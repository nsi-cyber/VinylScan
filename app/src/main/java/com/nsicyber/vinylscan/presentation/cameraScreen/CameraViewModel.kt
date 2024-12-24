package com.nsicyber.vinylscan.presentation.cameraScreen

import android.content.Context
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.common.InputImage
import com.nsicyber.vinylscan.domain.useCases.RecognizeBarcodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CameraViewModel @Inject constructor(
    private val recognizeBarcodeUseCase: RecognizeBarcodeUseCase,
) : ViewModel() {

    private val _cameraState = MutableStateFlow(CameraState())
    val cameraState: StateFlow<CameraState> = _cameraState.asStateFlow()


    fun onEvent(event: CameraEvent) {
        when (event) {



            is CameraEvent.TakePhoto -> takePhoto(
                applicationContext = event.applicationContext,
                controller = event.controller
            )
        }
    }





    private fun takePhoto(

        applicationContext: Context,
        controller: LifecycleCameraController,
    ) {
        controller.takePicture(
            ContextCompat.getMainExecutor(applicationContext),
            object : ImageCapture.OnImageCapturedCallback() {
                @OptIn(ExperimentalGetImage::class)
                override fun onCaptureSuccess(image: ImageProxy) {
                    super.onCaptureSuccess(image)
                    val inputImage = InputImage.fromMediaImage(
                        image.image!!,
                        image.imageInfo.rotationDegrees
                    )
                    viewModelScope.launch {
                        recognizeBarcodeUseCase(inputImage).collect { result ->
                            result.fold(
                                onSuccess = { detectedEmoji ->
                                   Log.d("",detectedEmoji.toString())
                                },
                                onFailure = {

                                }
                            )

                        }
                    }

                }

                override fun onError(exception: ImageCaptureException) {
                    super.onError(exception)
                    Log.e("Camera", "Couldn't take photo: ", exception)
                }
            }
        )
    }

}
