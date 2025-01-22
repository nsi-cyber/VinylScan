package com.nsicyber.vinylscan.presentation.favoriteScreen

import androidx.lifecycle.viewModelScope
import com.nsicyber.vinylscan.common.ApiResult
import com.nsicyber.vinylscan.domain.mapFunc.toVinylModel
import com.nsicyber.vinylscan.domain.useCases.GetReleaseDetailUseCase
import com.nsicyber.vinylscan.domain.useCases.favorite.GetFavoritesUseCase
import com.nsicyber.vinylscan.presentation.cameraScreen.showErrorMessage
import com.nsicyber.vinylscan.presentation.components.BaseViewModel
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
class FavoriteViewModel @Inject constructor(
    private val getFavoriteVinylsUseCase: GetFavoritesUseCase,
    private val getReleaseDetailUseCase: GetReleaseDetailUseCase,

    ) : BaseViewModel() {

    private val _favoriteScreenState = MutableStateFlow(FavoriteScreenState())
    val favoriteScreenState: StateFlow<FavoriteScreenState> = _favoriteScreenState.asStateFlow()

    fun onEvent(event: FavoriteScreenEvent) {
        when (event) {
            FavoriteScreenEvent.LoadScreen -> getFavorites()
            FavoriteScreenEvent.SetStateEmpty -> updateUiState {
                copy(
                    onSuccess = false,
                    isPageLoading = false,
                    favoriteVinyls = null
                )
            }

            is FavoriteScreenEvent.OpenDetail -> {
                getAlbumDiscogsDetail(
                    releaseId =
                    event.id
                )
            }

            FavoriteScreenEvent.DetailOpened -> updateUiState { copy(onSuccess = false) }

        }
    }

    private fun getAlbumDiscogsDetail(
        releaseId: Int?,

        ) {

        viewModelScope.launch {
            getReleaseDetailUseCase(releaseId).onStart {
                updateUiState { copy(isPageLoading = true) }
            }.onEach { result ->

                when (result) {
                    is ApiResult.Error -> {
                        showErrorMessage(this@FavoriteViewModel, result.message)
                        updateUiState {
                            copy(
                                isPageLoading = false,
                            )
                        }
                    }


                    is ApiResult.Success -> {

                        updateUiState {
                            copy(
                                isPageLoading = false,
                                onSuccess = true,
                                vinylModel = result.data?.toVinylModel()
                            )
                        }


                    }

                    null -> {


                    }
                }
            }.launchIn(this)
        }


    }

    private fun getFavorites() {
        viewModelScope.launch {
            updateUiState { copy(isPageLoading = true) }
            getFavoriteVinylsUseCase().collectLatest { favorites ->
                updateUiState {
                    copy(
                        isPageLoading = false,
                        favoriteVinyls = favorites,
                     )
                }
            }
        }
    }

    private fun updateUiState(block: FavoriteScreenState.() -> FavoriteScreenState) {
        _favoriteScreenState.update(block)
    }
} 