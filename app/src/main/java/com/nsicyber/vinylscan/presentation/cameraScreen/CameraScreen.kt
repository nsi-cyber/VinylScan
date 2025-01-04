package com.nsicyber.vinylscan.presentation.cameraScreen

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.nsicyber.vinylscan.presentation.MediaPlayerViewModel
import com.nsicyber.vinylscan.presentation.components.BaseView
import com.nsicyber.vinylscan.presentation.components.DeezerAlbumDetailBottomSheet
import com.nsicyber.vinylscan.presentation.components.DiscogsAlbumDetailBottomSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraScreen(
    cameraViewModel: CameraViewModel = hiltViewModel(),
    mediaPlayerViewModel: MediaPlayerViewModel = hiltViewModel(),
    applicationContext: Context,
) {
    val scope = rememberCoroutineScope()
    val state by cameraViewModel.cameraState.collectAsState()

    var hasCamPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasCamPermission = granted
        }
    )


    val bottomSheetState: BottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.Hidden,
            skipHiddenState = false
        ),
    )

    val controller = remember {
        LifecycleCameraController(applicationContext).apply {
            setEnabledUseCases(
                CameraController.IMAGE_ANALYSIS
            )
            setImageAnalysisAnalyzer(
                ContextCompat.getMainExecutor(applicationContext)
            ) { imageProxy ->
                cameraViewModel.onEvent(CameraEvent.TakePhoto(imageProxy))
            }

        }
    }

    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val lifecycleObserver = object : DefaultLifecycleObserver {
            override fun onStart(owner: LifecycleOwner) {
            }

            override fun onResume(owner: LifecycleOwner) {
            }

            override fun onPause(owner: LifecycleOwner) {
                mediaPlayerViewModel.pauseMediaPlayer()
            }

            override fun onStop(owner: LifecycleOwner) {
                mediaPlayerViewModel.pauseMediaPlayer()

            }
        }

        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
        }
    }


    LaunchedEffect(key1 = true) {
        launcher.launch(Manifest.permission.CAMERA)
    }

    LaunchedEffect(state.onSuccess) {
        if (state.onSuccess && (state.deezerAlbumDetail != null || state.discogsAlbumDetail != null)) {
            bottomSheetState.bottomSheetState.expand()
        }
    }

    LaunchedEffect(bottomSheetState.bottomSheetState.currentValue) {
        if (bottomSheetState.bottomSheetState.currentValue == SheetValue.PartiallyExpanded
            && (state.deezerAlbumDetail != null || state.discogsAlbumDetail != null)
        ) {
            cameraViewModel.onEvent(CameraEvent.SetStateEmpty)
            mediaPlayerViewModel.stopMediaPlayer()
        }
    }

    BaseView(
        bottomSheetState = bottomSheetState,
        bottomSheetContent = {
            if (state.deezerAlbumDetail != null && state.discogsAlbumDetail == null) {
                DeezerAlbumDetailBottomSheet(data = state.deezerAlbumDetail, mediaPlayerViewModel)
            } else if (state.deezerAlbumDetail == null && state.discogsAlbumDetail != null) {
                DiscogsAlbumDetailBottomSheet(data = state.discogsAlbumDetail)

            }


        },
        viewModel = cameraViewModel,
        isPageLoading = state.isPageLoading,
        content = {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                if (hasCamPermission) {
                    AndroidView(
                        factory = {
                            PreviewView(it).apply {
                                this.controller = controller
                                controller.bindToLifecycle(lifecycleOwner)
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Box(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .clip(
                                RoundedCornerShape(20.dp)
                            )
                            .align(Alignment.Center)
                            .background(Color.Gray.copy(alpha = 0.5f))
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                            repeat(40) {
                                Box(
                                    modifier = Modifier
                                        .height(50.dp)
                                        .width(2.dp)
                                        .background(Color.Black)
                                )
                            }
                        }
                        Text(
                            text = "Scan vinyl barcode...",
                            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 22.sp)
                        )

                    }
                }
            }
        }
    )
}



