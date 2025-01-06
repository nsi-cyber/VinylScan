package com.nsicyber.vinylscan.presentation.detailScreen

import android.app.Activity
import android.view.WindowManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import coil.compose.AsyncImage
import com.nsicyber.vinylscan.R
import com.nsicyber.vinylscan.common.BottomSheetType
import com.nsicyber.vinylscan.domain.model.VinylModel
import com.nsicyber.vinylscan.presentation.MediaPlayerViewModel
import com.nsicyber.vinylscan.presentation.cameraScreen.CameraEvent
import com.nsicyber.vinylscan.presentation.cameraScreen.CameraViewModel
import com.nsicyber.vinylscan.presentation.components.BarcodeBottomSheet
import com.nsicyber.vinylscan.presentation.components.BaseView
import com.nsicyber.vinylscan.presentation.components.TrackPreviewBottomSheet
import com.nsicyber.vinylscan.presentation.components.setScreenBrightness
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    cameraViewModel: CameraViewModel = hiltViewModel(),
    detailViewModel: DetailViewModel = hiltViewModel(),
    mediaPlayerViewModel: MediaPlayerViewModel = hiltViewModel(),
    data: VinylModel?,
    onBackPressed: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val detailState by detailViewModel.detailScreenState.collectAsState()
    val context = LocalContext.current

    val bottomSheetType = remember { mutableStateOf(BottomSheetType.PREVIEW) }

    val bottomSheetState: BottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.Hidden,
            skipHiddenState = false
        ),
    )

    LaunchedEffect(Unit) {
        cameraViewModel.onEvent(CameraEvent.SetStateEmpty)

    }
    LaunchedEffect(bottomSheetState.bottomSheetState.currentValue) {
        if (bottomSheetState.bottomSheetState.currentValue == SheetValue.PartiallyExpanded) {
            mediaPlayerViewModel.stopMediaPlayer()
            bottomSheetState.bottomSheetState.hide()
            (context as? Activity)?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            setScreenBrightness(
                context as Activity,
                WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
            )

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
                scope.launch {
                    bottomSheetState.bottomSheetState.hide()

                }
                detailViewModel.onEvent(DetailScreenEvent.SetStateEmpty)
                mediaPlayerViewModel.stopMediaPlayer()
                (context as? Activity)?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                setScreenBrightness(
                    context as Activity,
                    WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
                )

            }

            override fun onStop(owner: LifecycleOwner) {
                scope.launch {
                    bottomSheetState.bottomSheetState.hide()

                }
                detailViewModel.onEvent(DetailScreenEvent.SetStateEmpty)
                mediaPlayerViewModel.stopMediaPlayer()
                (context as? Activity)?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                setScreenBrightness(
                    context as Activity,
                    WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
                )

            }
        }

        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
        }
    }


    mediaPlayerViewModel.onFinish(onFinish = {
        scope.launch {
            bottomSheetState.bottomSheetState.hide()

        }
        detailViewModel.onEvent(DetailScreenEvent.SetStateEmpty)
        mediaPlayerViewModel.stopMediaPlayer()
    })

    LaunchedEffect(bottomSheetState.bottomSheetState.currentValue) {
        if (bottomSheetState.bottomSheetState.currentValue == SheetValue.PartiallyExpanded
            && (detailState.previewTrackModel != null)
        ) {
            detailViewModel.onEvent(DetailScreenEvent.SetStateEmpty)
            mediaPlayerViewModel.stopMediaPlayer()
            (context as? Activity)?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            setScreenBrightness(
                context as Activity,
                WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
            )

        }
    }

    BaseView(isPageLoading = detailState.isPageLoading,

        bottomSheetState = bottomSheetState,
        bottomSheetContent = {
            when (bottomSheetType.value) {
                BottomSheetType.PREVIEW -> TrackPreviewBottomSheet(
                    musicModel = detailState.previewTrackModel, mediaPlayerViewModel
                )

                BottomSheetType.BARCODE -> BarcodeBottomSheet(barcode = data?.barcode)
            }

        },

        content = {

            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                data?.cover?.takeIf { !it.isNullOrBlank() }?.let {
                    Box {
                        AsyncImage(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f),
                            model = it,
                            contentDescription = ""
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(16.dp)
                                .size(48.dp)
                                .clip(
                                    RoundedCornerShape(20.dp)
                                )

                                .background(Color.White.copy(alpha = 0.7f))

                                .clickable { onBackPressed() }
                                .padding(12.dp)
                        ) {
                            Image(
                                painter = painterResource(R.drawable.ic_arrow_left),
                                contentDescription = ""
                            )
                        }
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(16.dp)
                                .clip(
                                    RoundedCornerShape(20.dp)
                                )

                                .background(Color.White.copy(alpha = 0.7f))

                                .clickable {
                                    bottomSheetType.value = BottomSheetType.BARCODE
                                    scope.launch {
                                        bottomSheetState.bottomSheetState.expand()
                                    }

                                }
                                .padding(8.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Row(horizontalArrangement = Arrangement.spacedBy(1.dp)) {
                                    repeat(30) {
                                        Box(
                                            modifier = Modifier
                                                .height(30.dp)
                                                .width(2.dp)
                                                .background(Color.Black)
                                        )
                                    }
                                }
                                Text(
                                    text = stringResource(R.string.show_barcode),
                                    color = Color.Black
                                )
                            }

                        }
                    }

                }



                Column(modifier = Modifier.padding(start = 16.dp, bottom = 4.dp)) {
                    data?.title?.takeIf { !it.isNullOrBlank() }?.let {
                        Text(
                            text = it,
                            color = Color.White,
                            fontSize = 32.sp,
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.Normal,
                            lineHeight = 32.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    data?.artistName?.takeIf { !it.isNullOrBlank() }?.let {
                        Text(
                            lineHeight = 24.sp,
                            text = it,
                            color = Color.Gray,
                            fontSize = 24.sp,

                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                data?.genres?.takeIf { !it.isNullOrEmpty() }?.let {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = stringResource(R.string.genre),
                            color = Color.Gray,
                            fontSize = 16.sp,

                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp),
                        )
                        Text(
                            text = it,
                            color = Color.White,
                            fontSize = 18.sp,
                            lineHeight = 18.sp,

                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp),
                        )

                    }
                }

                data?.releaseDate?.takeIf { !it.isNullOrBlank() }?.let {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp),
                            text = stringResource(R.string.year),
                            color = Color.Gray,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.Normal,
                        )
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp),
                            text = it,
                            color = Color.White,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.Normal,
                        )
                    }

                }

                data?.styles?.takeIf { !it.isNullOrEmpty() }?.let {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = stringResource(R.string.style),
                            color = Color.Gray,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp),
                        )

                        Text(
                            text = it,
                            color = Color.White,
                            fontSize = 18.sp,
                            lineHeight = 18.sp,

                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp),
                        )

                    }
                }
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    data?.tracks?.takeIf { !it.isNullOrEmpty() }
                        ?.let {

                            repeat(it?.size ?: 0) { trackIndex ->
                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(20.dp))
                                        .clickable {
                                            detailViewModel.onEvent(DetailScreenEvent.SetStateEmpty)
                                            mediaPlayerViewModel.stopMediaPlayer()
                                            bottomSheetType.value = BottomSheetType.PREVIEW
                                            detailViewModel.onEvent(
                                                DetailScreenEvent.LoadScreen(
                                                    query = "${
                                                        it?.get(
                                                            trackIndex
                                                        )?.artistName
                                                    } ${it?.get(trackIndex)?.title}"
                                                )
                                            )
                                            scope.launch {
                                                bottomSheetState.bottomSheetState.expand()
                                            }

                                        }
                                        .padding(horizontal = 16.dp, vertical = 4.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        modifier = Modifier.size(32.dp),
                                        painter = painterResource(R.drawable.ic_play),
                                        colorFilter = ColorFilter.tint(Color.White),
                                        contentDescription = ""
                                    )
                                    Column(
                                        modifier = Modifier
                                    ) {
                                        Text(
                                            text = it?.get(trackIndex)?.title.orEmpty(),
                                            color = Color.White,
                                            fontSize = 20.sp,
                                            lineHeight = 20.sp,

                                            textAlign = TextAlign.Start,
                                            fontWeight = FontWeight.Normal,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                        Text(
                                            lineHeight = 24.sp,
                                            text = it?.get(trackIndex)?.duration.orEmpty(),
                                            color = Color.Gray,
                                            fontSize = 14.sp,
                                            textAlign = TextAlign.Start,
                                            fontWeight = FontWeight.Normal,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp, vertical = 2.dp)
                                        .fillMaxWidth()
                                        .height(1.dp)
                                        .background(Color.White.copy(alpha = 0.1f))
                                )


                            }
                        }
                }

            }

        }
    )
}