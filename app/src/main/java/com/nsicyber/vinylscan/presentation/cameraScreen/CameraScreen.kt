package com.nsicyber.vinylscan.presentation.cameraScreen

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.nsicyber.vinylscan.domain.model.AlbumModel
import com.nsicyber.vinylscan.presentation.components.BaseView
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraScreen(
    cameraViewModel: CameraViewModel = hiltViewModel(),
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

    LaunchedEffect(key1 = true) {
        launcher.launch(Manifest.permission.CAMERA)
    }

    val bottomSheetState: BottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.Hidden,
            skipHiddenState = false
        ),
    )

    val controller = remember {
        LifecycleCameraController(applicationContext).apply {
            setEnabledUseCases(CameraController.IMAGE_ANALYSIS
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
        if (state.onSuccess == true && state.albumDetail != null) {
            bottomSheetState.bottomSheetState.expand()
        }
    }

    LaunchedEffect(bottomSheetState.bottomSheetState.currentValue) {
        if(bottomSheetState.bottomSheetState.currentValue==SheetValue.PartiallyExpanded&&state.albumDetail!=null)
            cameraViewModel.onEvent(CameraEvent.SetStateEmpty)
    }

    BaseView(
        bottomSheetState = bottomSheetState,
        bottomSheetContent = {
            AlbumDetailBottomSheet(data = state.albumDetail)
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
            }
        }
    )
}


@Composable
fun AlbumDetailBottomSheet(
    data: AlbumModel?,
) {
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            model = data?.imageUrl,
            contentDescription = ""
        )


        Column(modifier = Modifier.padding(start = 16.dp)) {
            Text(
                text = data?.name.orEmpty(),
                color = Color.White,
                fontSize = 28.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                lineHeight = 24.sp,
                text = data?.artistName.orEmpty(),
                color = Color.Gray,
                fontSize = 22.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = data?.year.toString(),
            color = Color.White,
            fontSize = 28.sp,
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.Bold,
        )


        Column(modifier = Modifier.padding(start = 16.dp)) {
            Text(
                text = data?.genres?.joinToString(", ").orEmpty(),
                color = Color.White,
                fontSize = 28.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                lineHeight = 24.sp,
                text = data?.styles?.joinToString(", ").orEmpty(),
                color = Color.Gray,
                fontSize = 22.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.fillMaxWidth()
            )
        }

        repeat(data?.tracks?.size ?: 0) { trackIndex ->
            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(
                    text = data?.tracks?.get(trackIndex)?.title.orEmpty(),
                    color = Color.White,
                    fontSize = 22.sp,
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    lineHeight = 24.sp,
                    text = data?.tracks?.get(trackIndex)?.duration.orEmpty(),
                    color = Color.Gray,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.fillMaxWidth()
                )
            }

        }
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