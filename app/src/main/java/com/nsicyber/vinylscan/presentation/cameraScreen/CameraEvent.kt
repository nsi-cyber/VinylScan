package com.nsicyber.vinylscan.presentation.cameraScreen

import android.content.Context
import androidx.camera.view.LifecycleCameraController

sealed class CameraEvent {


    data class TakePhoto(
        val applicationContext: Context,
        val controller: LifecycleCameraController
    ) : CameraEvent()

}