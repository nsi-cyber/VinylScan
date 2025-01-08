package com.nsicyber.vinylscan.presentation.cameraScreen

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.view.WindowManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.nsicyber.vinylscan.R
import com.nsicyber.vinylscan.domain.model.VinylModel
import com.nsicyber.vinylscan.presentation.components.BaseView
import com.nsicyber.vinylscan.presentation.components.LottieView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraScreen(
    cameraViewModel: CameraViewModel = hiltViewModel(),
    applicationContext: Context,
    navigateToDetail: (data: VinylModel?) -> Unit,
    navigateToSearch: () -> Unit,
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
    val context = LocalContext.current
    DisposableEffect(Unit) {
        val window = (context as? Activity)?.window
        window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        onDispose {
            window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

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

    LaunchedEffect(state.onSuccess) {
        if (state.onSuccess && state.vinylModel != null) {
            navigateToDetail(state.vinylModel)

        }
    }

    LaunchedEffect(key1 = true) {
        launcher.launch(Manifest.permission.CAMERA)
    }




    BaseView(

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
                    var columnSize by remember { mutableStateOf(IntSize.Zero) }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .align(Alignment.Center)
                            .padding(32.dp)
                            .onGloballyPositioned { layoutCoordinates ->
                                columnSize = layoutCoordinates.size
                            },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                            repeat(40) {
                                Box(
                                    modifier = Modifier
                                        .alpha(0.3f)
                                        .height(50.dp)
                                        .width(2.dp)
                                        .background(Color.Black)
                                )
                            }
                        }
                        Text(
                            text = stringResource(R.string.scan_vinyl_barcode),
                            style = TextStyle(
                                fontWeight = FontWeight.Light,
                                fontSize = 22.sp,
                                color = Color.Black
                            )
                        )
                    }

                    Box(
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        LottieView(
                            modifier = Modifier
                                .size(
                                    columnSize.width.dp,
                                    columnSize.height.dp
                                )
                                .padding(20.dp),
                            res = R.raw.scan_anim
                        )
                    }

                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(16.dp)
                            .size(48.dp)
                            .clip(
                                RoundedCornerShape(20.dp)
                            )

                            .background(Color.White.copy(alpha = 0.7f))

                            .clickable { navigateToSearch() }
                            .padding(12.dp)
                    ) {
                        Image(
                            imageVector = Icons.Default.Search,
                            contentDescription = ""
                        )
                    }


                }
            }
        }
    )
}



