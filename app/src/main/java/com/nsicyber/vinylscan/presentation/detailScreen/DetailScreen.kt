package com.nsicyber.vinylscan.presentation.detailScreen

import android.app.Activity
import android.content.Context
import android.view.WindowManager
import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.nsicyber.vinylscan.R
import com.nsicyber.vinylscan.common.BottomSheetType
import com.nsicyber.vinylscan.common.formatDate
import com.nsicyber.vinylscan.domain.model.VinylModel
import com.nsicyber.vinylscan.presentation.MediaPlayerViewModel
import com.nsicyber.vinylscan.presentation.cameraScreen.CameraEvent
import com.nsicyber.vinylscan.presentation.cameraScreen.CameraViewModel
import com.nsicyber.vinylscan.presentation.components.BarcodeBottomSheet
import com.nsicyber.vinylscan.presentation.components.BaseView
import com.nsicyber.vinylscan.presentation.components.TrackPreviewBottomSheet
import com.nsicyber.vinylscan.presentation.components.setScreenBrightness
import com.simonsickle.compose.barcodes.BarcodeType
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


    LaunchedEffect(detailState.onBottomSheetError) {
        if (detailState.onBottomSheetError == true) {

            Toast.makeText(context,"Track data cant find",Toast.LENGTH_SHORT).show()
            scope.launch {
                bottomSheetState.bottomSheetState.hide()

            }
            detailViewModel.onEvent(DetailScreenEvent.SetStateEmpty)
            mediaPlayerViewModel.stopMediaPlayer()
        }
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


                data?.images?.takeIf { !it.isNullOrEmpty() }?.let {
                    Box {
                        FlipGallery(it)
                        // AsyncImage(modifier = Modifier.fillMaxWidth().aspectRatio(1f), model = it, contentDescription = "")
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

                        if (BarcodeType.EAN_13.isValueValid(data.barcode ?: "")) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(16.dp)
                                    .height(48.dp).aspectRatio(3/2f)
                                    .clip(
                                        RoundedCornerShape(20.dp)
                                    )

                                    .background(Color.White.copy(alpha = 0.7f))

                                    .clickable {
                                        bottomSheetType.value = BottomSheetType.BARCODE
                                        scope.launch {
                                            bottomSheetState.bottomSheetState.expand()
                                        }

                                    }                                    .padding(6.dp)
                            ) {
                                Image(modifier = Modifier.fillMaxSize(),
                                    painter = painterResource(R.drawable.ic_show_barcode),
                                    contentDescription = ""
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
                data?.catalog?.takeIf { !it.isNullOrBlank() }?.let {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp),
                            text = "Catalog",
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
                data?.vinylQuantity?.let {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp),
                            text = "Vinyl Count",
                            color = Color.Gray,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.Normal,
                        )
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp),
                            text = it.toString(),
                            color = Color.White,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.Normal,
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
                            text = formatDate(it),
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
                data?.totalTime?.takeIf { !it.isNullOrEmpty() }?.let {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Toplam Süre",
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
                                        it?.get(trackIndex)?.position?.takeIf { !it.isNullOrBlank() }
                                            ?.let { duration ->
                                                Text(
                                                    lineHeight = 24.sp,
                                                    text = duration,
                                                    color = Color.Gray,
                                                    fontSize = 14.sp,
                                                    textAlign = TextAlign.Start,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier.fillMaxWidth()
                                                )
                                            }

                                        Text(
                                            text = it?.get(trackIndex)?.title.orEmpty(),
                                            color = Color.White,
                                            fontSize = 20.sp,
                                            lineHeight = 20.sp,

                                            textAlign = TextAlign.Start,
                                            fontWeight = FontWeight.Normal,
                                            modifier = Modifier.fillMaxWidth()
                                        )

                                        it?.get(trackIndex)?.duration?.takeIf { !it.isNullOrBlank() }
                                            ?.let { duration ->
                                                Text(
                                                    lineHeight = 24.sp,
                                                    text = duration,
                                                    color = Color.Gray,
                                                    fontSize = 14.sp,
                                                    textAlign = TextAlign.Start,
                                                    fontWeight = FontWeight.Normal,
                                                    modifier = Modifier.fillMaxWidth()
                                                )
                                            }
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

@Composable
fun FlipGallery(imageUrls: List<String?>) {
    var currentIndex by remember { mutableStateOf(0) }
    val flipAngle = remember { Animatable(0f) }
    val density = LocalDensity.current.density
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(imageUrls) {
        preloadImages(context, imageUrls)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .background(Color.Black)
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        // Parmağı bıraktığınızda hedef açıyı belirle
                        val targetAngle = when {
                            flipAngle.value > 90 -> 180f
                            flipAngle.value < -90 -> -180f
                            else -> 0f
                        }

                        // Animasyonu başlat
                        scope.launch {
                            flipAngle.animateTo(
                                targetValue = targetAngle,
                                animationSpec = tween(
                                    durationMillis = 300,
                                    easing = FastOutSlowInEasing
                                )
                            )

                            // Sayfa değişimi ve sıfırlama
                            if (targetAngle == 180f) {
                                currentIndex = (currentIndex + 1) % imageUrls.size
                            } else if (targetAngle == -180f) {
                                currentIndex = (currentIndex - 1 + imageUrls.size) % imageUrls.size
                            }
                            flipAngle.snapTo(0f) // Yeni resim için açıyı sıfırla
                        }
                    },
                    onHorizontalDrag = { _, dragAmount ->
                        // El hareketine göre açıyı güncelle
                        scope.launch {
                            flipAngle.snapTo(
                                (flipAngle.value + dragAmount / 3).coerceIn(-180f, 180f)
                            )
                        }

                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .graphicsLayer(
                    rotationY = flipAngle.value,
                    cameraDistance = 8 * density,
                    scaleX = if (flipAngle.value > 90f || flipAngle.value < -90f) -1f else 1f // Aynalama düzeltme
                )
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (flipAngle.value <= 90 && flipAngle.value >= -90) {
                // Ön yüz
                AsyncImage(
                    model = imageUrls[currentIndex],
                    contentDescription = "Image $currentIndex",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                // Arka yüz (bir sonraki veya önceki resim)
                val nextIndex = if (flipAngle.value > 0) {
                    (currentIndex + 1) % imageUrls.size
                } else {
                    (currentIndex - 1 + imageUrls.size) % imageUrls.size
                }
                AsyncImage(
                    model = imageUrls[nextIndex],
                    contentDescription = "Image $nextIndex",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .clip(
                    RoundedCornerShape(20.dp)
                )

                .background(Color.White.copy(alpha = 0.7f))
                .padding(horizontal = 8.dp, vertical = 6.dp)
        ) {
            Text(
                text = "${currentIndex + 1}/${imageUrls.size}",
                color = Color.Black,
                fontSize = 14.sp,
                style = TextStyle(fontWeight = FontWeight.Bold, textAlign = TextAlign.Center),
            )
        }

    }
}

fun preloadImages(context: Context, imageUrls: List<String?>) {
    val imageLoader = ImageLoader(context)
    imageUrls.forEach { url ->
        val request = ImageRequest.Builder(context)
            .data(url)
            .build()
        imageLoader.enqueue(request) // Resimleri önceden yükle
    }
}

