package com.nsicyber.vinylscan.presentation.cameraScreen

import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.common.InputImage
import com.nsicyber.vinylscan.common.ApiResult
import com.nsicyber.vinylscan.domain.mapFunc.toAlbumModel
import com.nsicyber.vinylscan.domain.useCases.GetDetailUseCase
import com.nsicyber.vinylscan.domain.useCases.RecognizeBarcodeUseCase
import com.nsicyber.vinylscan.domain.useCases.SearchBarcodeUseCase
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
class CameraViewModel @Inject constructor(
    private val recognizeBarcodeUseCase: RecognizeBarcodeUseCase,
    private val getDetailUseCase: GetDetailUseCase,
    private val searchBarcodeUseCase: SearchBarcodeUseCase,

    ) : BaseViewModel() {

    private val _cameraState = MutableStateFlow(CameraState())
    val cameraState: StateFlow<CameraState> = _cameraState.asStateFlow()


    fun onEvent(event: CameraEvent) {
        when (event) {


            is CameraEvent.TakePhoto -> takePhoto(
                event.imageProxy
            )

            CameraEvent.SetStateEmpty -> {
                updateUiState { copy(onSuccess = false, isPageLoading = false, albumDetail = null) }
            }
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
                if (cameraState.value.isPageLoading == false&&cameraState.value.albumDetail==null) {
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
                //  updateUiState { copy(isPageLoading = false) }
                imageProxy.close()
            }
        }
    }


    private fun searchBarcode(
        barcode: String?,
    ) {
        barcode?.let {
            viewModelScope.launch {
                searchBarcodeUseCase(barcode).onStart {
                    updateUiState {
                        copy(
                            isPageLoading = true,
                            albumDetail = null,
                            onSuccess = false
                        )
                    }
                }.onEach { result ->

                    when (result) {
                        is ApiResult.Error -> {
                            showErrorMessage(this@CameraViewModel, result.message)
                            updateUiState {
                                copy(
                                    isPageLoading = false,

                                    )
                            }
                        }


                        is ApiResult.Success ->
                            getData(
                                masterId = result.data?.results?.firstOrNull()?.master_id,
                                thumbnail = result.data?.results?.firstOrNull()?.cover_image
                            )

                        null -> {

                        }
                    }
                }.launchIn(this)
            }
        } ?: run {
            updateUiState {
                copy(
                    isPageLoading = false,
                    onSuccess = false, albumDetail = null
                )
            }
        }


    }

    private fun getData(
        masterId: Int?,
        thumbnail: String?
    ) {
        masterId?.let {
            viewModelScope.launch {
                getDetailUseCase(masterId).onStart {
                    updateUiState { copy(isPageLoading = true) }
                }.onEach { result ->

                    when (result) {
                        is ApiResult.Error -> {
                            showErrorMessage(this@CameraViewModel, result.message)
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
                                    albumDetail = result.data.toAlbumModel(thumbnail),
                                )
                            }

                        null -> {


                        }
                    }
                }.launchIn(this)
            }
        }?:run {      updateUiState {
            copy(
                isPageLoading = false,
                onSuccess = false, albumDetail = null
            )
        } }


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