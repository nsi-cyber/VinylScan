package com.nsicyber.vinylscan.presentation.cameraScreen

import androidx.camera.core.ImageProxy

sealed class CameraEvent {


    data class TakePhoto(
        val imageProxy: ImageProxy
    ) : CameraEvent()

    data object SetStateEmpty : CameraEvent()

}