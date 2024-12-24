package com.nsicyber.vinylscan.presentation.cameraScreen

import android.content.Context
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController

sealed class CameraEvent {


    data class TakePhoto(
        val imageProxy: ImageProxy
    ) : CameraEvent()
    data object SetStateEmpty : CameraEvent()

}