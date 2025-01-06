package com.nsicyber.vinylscan.presentation.detailScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nsicyber.vinylscan.common.ApiResult
import com.nsicyber.vinylscan.domain.useCases.SearchUseCase
import com.nsicyber.vinylscan.presentation.components.BaseViewModel
import com.nsicyber.vinylscan.presentation.components.SnackbarController
import com.nsicyber.vinylscan.presentation.components.SnackbarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
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
class DetailViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase,
) : BaseViewModel() {

    private val _detailScreenState = MutableStateFlow(DetailScreenState())
    val detailScreenState: StateFlow<DetailScreenState> = _detailScreenState.asStateFlow()


    fun onEvent(event: DetailScreenEvent) {
        when (event) {


            is DetailScreenEvent.LoadScreen -> getTrackData(event.query)
            DetailScreenEvent.SetStateEmpty -> updateUiState {
                copy(
                    onSuccess = false,
                    isPageLoading = false,
                    previewTrackModel = null
                )
            }
        }
    }

    private fun getTrackData(
        query: String?,

        ) {

        viewModelScope.launch {
            searchUseCase(query).onStart {
                updateUiState { copy(isPageLoading = true) }
            }.onEach { result ->

                when (result) {
                    is ApiResult.Error -> {
                        showErrorMessage(this@DetailViewModel, result.message)
                        updateUiState {
                            copy(
                                isPageLoading = false,
                            )
                        }
                    }


                    is ApiResult.Success -> {
                        updateUiState {
                            copy(
                                onSuccess = true,
                                isPageLoading = false,
                                previewTrackModel =

                                PreviewTrackModel(
                                    artistName = result.data?.data?.firstOrNull()?.artist?.name,
                                    title = result.data?.data?.firstOrNull()?.title,
                                    album = result.data?.data?.firstOrNull()?.album?.title,
                                    preview = result.data?.data?.firstOrNull()?.preview,
                                    cover = result.data?.data?.firstOrNull()?.md5_image
                                )
                            )
                        }
                    }

                    null -> {


                    }
                }
            }.launchIn(this)
        }
    }


    private fun updateUiState(block: DetailScreenState.() -> DetailScreenState) {
        _detailScreenState.update(block)
    }
}

fun showErrorMessage(viewModel: ViewModel, message: String) {
    viewModel.viewModelScope.launch {
        SnackbarController.sendEvent(event = SnackbarEvent(message = message))
    }

}