package com.nsicyber.vinylscan.presentation.cameraScreen

import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.common.InputImage
import com.nsicyber.vinylscan.common.ApiResult
import com.nsicyber.vinylscan.data.datastore.AppPreferences
import com.nsicyber.vinylscan.domain.mapFunc.toDatabase
import com.nsicyber.vinylscan.domain.mapFunc.toVinylModel
import com.nsicyber.vinylscan.domain.useCases.GetReleaseDetailUseCase
import com.nsicyber.vinylscan.domain.useCases.InsertRecentlyViewedUseCase
import com.nsicyber.vinylscan.domain.useCases.RecognizeBarcodeUseCase
import com.nsicyber.vinylscan.domain.useCases.SearchBarcodeUseCase
import com.nsicyber.vinylscan.presentation.components.BaseViewModel
import com.nsicyber.vinylscan.presentation.components.SnackbarController
import com.nsicyber.vinylscan.presentation.components.SnackbarEvent
import com.nsicyber.vinylscan.utils.ReviewManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CameraViewModel @Inject constructor(
    private val recognizeBarcodeUseCase: RecognizeBarcodeUseCase,
    private val insertRecentlyViewedUseCase: InsertRecentlyViewedUseCase,
    private val getReleaseDetailUseCase: GetReleaseDetailUseCase,
    private val searchBarcodeUseCase: SearchBarcodeUseCase,
    private val appPreferences: AppPreferences,
    private val reviewManager: ReviewManager
) : BaseViewModel() {

    private val _cameraState = MutableStateFlow(CameraState())
    val cameraState: StateFlow<CameraState> = _cameraState.asStateFlow()

    fun onEvent(event: CameraEvent) {
        when (event) {
            is CameraEvent.TakePhoto -> takePhoto(event.imageProxy)
            CameraEvent.SetStateEmpty -> {
                updateUiState {
                    copy(
                        onSuccess = false,
                        isPageLoading = false,
                    )
                }
            }
            is CameraEvent.ShowReviewDialog -> reviewManager.initReviewFlow(event.activity)
        }
    }

    @OptIn(ExperimentalGetImage::class)
    private fun takePhoto(imageProxy: ImageProxy) {
        viewModelScope.launch {
            try {
                val inputImage = InputImage.fromMediaImage(
                    imageProxy.image!!,
                    imageProxy.imageInfo.rotationDegrees
                )
                if (cameraState.value.onSuccess == false && cameraState.value.isPageLoading == false) {
                    recognizeBarcodeUseCase(inputImage).collect { result ->
                        result.fold(
                            onSuccess = { barcode ->
                                searchBarcode(barcode)
                            },
                            onFailure = {
                                Log.e("CameraViewModel", "Barcode analysis failed.")
                            }
                        )
                    }
                }

            } catch (e: Exception) {
                Log.e("CameraViewModel", "Error analyzing image: ${e.message}")
            } finally {
                imageProxy.close()
            }
        }
    }

    private fun searchBarcode(barcode: String?) {
        barcode?.let {
            viewModelScope.launch {
                searchBarcodeUseCase(barcode).onStart {
                    updateUiState {
                        copy(
                            isPageLoading = true,
                            vinylModel = null,
                            onSuccess = false
                        )
                    }
                }.onEach { result ->
                    when (result) {
                        is ApiResult.Error -> {
                            showErrorMessage(this@CameraViewModel, result.message)
                            updateUiState {
                                copy(isPageLoading = false)
                            }
                        }
                        is ApiResult.Success -> {
                            getReleaseDiscogsDetail(
                                releaseId = result.data?.results?.firstOrNull()?.id,
                            )
                        }
                        null -> {}
                    }
                }.launchIn(this)
            }
        } ?: run {
            updateUiState {
                copy(
                    isPageLoading = false,
                    onSuccess = false,
                    vinylModel = null
                )
            }
        }
    }

    private fun getReleaseDiscogsDetail(releaseId: Int?) {
        viewModelScope.launch {
            getReleaseDetailUseCase(releaseId).onStart {
                updateUiState { copy(isPageLoading = true) }
            }.onEach { result ->
                when (result) {
                    is ApiResult.Error -> {
                        showErrorMessage(this@CameraViewModel, result.message)
                        updateUiState {
                            copy(isPageLoading = false)
                        }
                    }
                    is ApiResult.Success -> {
                        insertRecentlyViewedUseCase(item = result.data.toDatabase()).collect()
                        appPreferences.incrementSuccessfulScanCount()
                        
                        val scanCount = appPreferences.successfulScanCount.first()
                        if (scanCount % 5 == 0) {
                            updateUiState {
                                copy(
                                    isPageLoading = false,
                                    onSuccess = true,
                                    vinylModel = result.data?.toVinylModel(),
                                    shouldShowReview = true
                                )
                            }
                        } else {
                            updateUiState {
                                copy(
                                    isPageLoading = false,
                                    onSuccess = true,
                                    vinylModel = result.data?.toVinylModel(),
                                    shouldShowReview = false
                                )
                            }
                        }
                    }
                    null -> {}
                }
            }.launchIn(this)
        }
    }

    private fun updateUiState(block: CameraState.() -> CameraState) {
        _cameraState.update(block)
    }
}

fun showErrorMessage(viewModel: ViewModel, message: String) {
    viewModel.viewModelScope.launch {
        SnackbarController.sendEvent(event = SnackbarEvent(message = message))
    }
}