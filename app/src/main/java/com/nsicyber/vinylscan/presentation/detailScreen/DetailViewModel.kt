package com.nsicyber.vinylscan.presentation.detailScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nsicyber.vinylscan.common.ApiResult
import com.nsicyber.vinylscan.domain.model.FavoriteVinylModel
import com.nsicyber.vinylscan.domain.model.VinylModel
import com.nsicyber.vinylscan.domain.useCases.SearchUseCase
import com.nsicyber.vinylscan.domain.useCases.favorite.AddToFavoritesUseCase
import com.nsicyber.vinylscan.domain.useCases.favorite.IsFavoriteUseCase
import com.nsicyber.vinylscan.domain.useCases.favorite.RemoveFromFavoritesUseCase
import com.nsicyber.vinylscan.presentation.components.BaseViewModel
import com.nsicyber.vinylscan.presentation.components.SnackbarController
import com.nsicyber.vinylscan.presentation.components.SnackbarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase,
    private val addToFavoritesUseCase: AddToFavoritesUseCase,
    private val removeFromFavoritesUseCase: RemoveFromFavoritesUseCase,
    private val isFavoriteUseCase: IsFavoriteUseCase
) : BaseViewModel() {

    private val _detailScreenState = MutableStateFlow(DetailScreenState())
    val detailScreenState: StateFlow<DetailScreenState> = _detailScreenState.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite

    fun onEvent(event: DetailScreenEvent) {
        when (event) {
            is DetailScreenEvent.LoadTrack -> {

                getTrackData(event.query)
            }
            DetailScreenEvent.SetStateEmpty -> updateUiState {
                copy(
                    onSuccess = false,
                    isPageLoading = false,
                    previewTrackModel = null
                )
            }

            is DetailScreenEvent.ToggleFavorite -> toggleFavorite(event.vinyl)
            is DetailScreenEvent.LoadScreen ->                 checkFavoriteStatus(event.vinylId)

        }
    }

    private fun getTrackData(
        query: String?,
    ) {
        viewModelScope.launch {
            searchUseCase(query).onStart {
                updateUiState { copy(isPageLoading = true, onBottomSheetError = false) }
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
                                ),
                                onBottomSheetError = if (result?.data?.data?.firstOrNull() == null) true else false
                            )
                        }
                    }
                    null -> {
                    }
                }
            }.launchIn(this)
        }
    }

    fun checkFavoriteStatus(vinylId: Int?) {
        viewModelScope.launch {
            isFavoriteUseCase(vinylId?:0).collectLatest {
                _isFavorite.value = it
            }
        }
    }

    fun toggleFavorite(vinyl: VinylModel) {
        viewModelScope.launch {
            if (_isFavorite.value) {
                removeFromFavoritesUseCase(vinyl.id ?: return@launch)
            } else {
                addToFavoritesUseCase(
                    FavoriteVinylModel(
                        vinylId = vinyl.id ?: return@launch,
                        title = vinyl.artistName +" - "+vinyl.title ,
                        releaseDate = vinyl.year ?: return@launch,
                        image = vinyl.images?.firstOrNull()
                    )
                )
                checkFavoriteStatus(vinyl.id)
            }
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