package com.nsicyber.vinylscan.presentation.cameraScreen

import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.nsicyber.vinylscan.ui.theme.IconLeftArrow
import com.nsicyber.vinylscan.ui.theme.IconPhotoShoot
import com.nsicyber.vinylscan.ui.theme.IconRotate

@Composable
fun CameraScreen(
    cameraViewModel: CameraViewModel = hiltViewModel(),
    applicationContext: Context,
    onBackPressed: () -> Unit
) {

    val state by cameraViewModel.cameraState.collectAsState()

    LaunchedEffect(state.onSuccess) {
        if (state.onSuccess == true) {
            onBackPressed()
        }
    }

    val controller = remember {
        LifecycleCameraController(applicationContext).apply {
            setEnabledUseCases(
                CameraController.IMAGE_CAPTURE
            )
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AndroidView(
            factory = {
                PreviewView(it).apply {
                    this.controller = controller
                    controller.bindToLifecycle(lifecycleOwner)
                }
            },
            modifier = Modifier.fillMaxSize()
        )



        CustomButton(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 32.dp)
                .align(Alignment.BottomStart), onClick = {
                onBackPressed()
            }, icon = IconLeftArrow
        )

        CustomButton(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 32.dp)
                .align(Alignment.BottomEnd), onClick = {
                controller.cameraSelector =
                    if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                        CameraSelector.DEFAULT_FRONT_CAMERA
                    } else CameraSelector.DEFAULT_BACK_CAMERA
            }, icon = IconRotate
        )





        CustomButton(modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 32.dp)
            .align(Alignment.BottomCenter), icon = IconPhotoShoot,
            onClick = {

                cameraViewModel.onEvent(
                        CameraEvent.TakePhoto(
                            controller = controller,
                            applicationContext = applicationContext,
                        )
                    )




            }
        )
    }


}


@Composable
fun CustomButton(modifier: Modifier, icon: Int, onClick: () -> Unit) {


    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .background(Color.White)

            .size(64.dp)
            .padding(12.dp)
    ) {


        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(icon),
            contentDescription = "",
            colorFilter = ColorFilter.tint(Color.Black)
        )

    }
}