package com.nsicyber.vinylscan.presentation.searchScreen


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nsicyber.vinylscan.common.ApiResult
import com.nsicyber.vinylscan.domain.mapFunc.getBarcodeFromList
import com.nsicyber.vinylscan.domain.mapFunc.toVinylModel
import com.nsicyber.vinylscan.domain.useCases.GetDetailUseCase
import com.nsicyber.vinylscan.domain.useCases.SearchVinylUseCase
import com.nsicyber.vinylscan.presentation.cameraScreen.showErrorMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SearchScreenViewModel @Inject
constructor(
    private val searchVinylUseCase: SearchVinylUseCase,
    private val getDetailUseCase: GetDetailUseCase,

    ) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchScreenState())
    val uiState: StateFlow<SearchScreenState> get() = _uiState.asStateFlow()

    private var searchJob: Job? = null


    fun onEvent(event: SearchScreenEvent) {
        when (event) {

            is SearchScreenEvent.Search -> {
                updateUiState { copy(searchQuery = event.query) }
                if (event.query.length >= 3)
                    searchWithDebounce(event.query)
                else if (event.query.isEmpty())
                    setStateEmpty()
            }

            SearchScreenEvent.SetStateEmpty -> {
                setStateEmpty()
            }

            is SearchScreenEvent.OpenDetail -> getAlbumDiscogsDetail(
                masterId = _uiState.value.searchSearchResultItem?.get(
                    event.index
                )?.master_id,
                thumbnail = _uiState.value.searchSearchResultItem?.get(event.index)?.cover_image,
                barcode = _uiState.value.searchSearchResultItem?.get(event.index)?.barcode
            )
        }
    }


    private fun getAlbumDiscogsDetail(
        masterId: Int?,
        thumbnail: String?,
        barcode: List<String?>?
    ) {

        viewModelScope.launch {
            getDetailUseCase(masterId).onStart {
                updateUiState { copy(isPageLoading = true) }
            }.onEach { result ->

                when (result) {
                    is ApiResult.Error -> {
                        showErrorMessage(this@SearchScreenViewModel, result.message)
                        updateUiState {
                            copy(
                                isPageLoading = false,
                            )
                        }
                    }


                    is ApiResult.Success ->
                        updateUiState {
                            copy(
                                isPageLoading = false,
                                onSuccess = true,
                                vinylModel = result.data?.toVinylModel(
                                    thumbnail = thumbnail,
                                    barcode = barcode
                                )
                            )
                        }

                    null -> {


                    }
                }
            }.launchIn(this)
        }


    }

    private fun getSearchResults(query: String) {
        viewModelScope.launch {
            searchVinylUseCase(query).onStart {
                updateUiState { copy(isPageLoading = true) }

            }.onEach { result ->

                when (result) {

                    is ApiResult.Success -> {

                        updateUiState {
                            copy(
                                searchSearchResultItem = result.data?.results
                                    ?.filter { !it?.barcode.isNullOrEmpty() }
                                    ?.filter { !it?.cover_image.isNullOrBlank() }
                                    ?.distinctBy { getBarcodeFromList(it?.barcode) },
                                isPageLoading = false
                            )
                        }
                    }

                    is ApiResult.Error -> {


                    }

                    null -> {

                    }
                }
            }.launchIn(this)
        }


    }


    private fun searchWithDebounce(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300)
            getSearchResults(query)
        }
    }

    private fun setStateEmpty() {
        searchJob?.cancel()
        updateUiState { copy(searchSearchResultItem = null, searchQuery = "", onSuccess = false) }
    }


    private fun updateUiState(update: SearchScreenState.() -> SearchScreenState) {
        _uiState.update { it.update() }
    }


}

