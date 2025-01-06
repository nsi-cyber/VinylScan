package com.nsicyber.vinylscan.presentation.components

import android.app.Activity
import android.view.WindowManager
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.nsicyber.vinylscan.R
import com.nsicyber.vinylscan.presentation.MediaPlayerViewModel
import com.nsicyber.vinylscan.presentation.detailScreen.PreviewTrackModel
import com.simonsickle.compose.barcodes.Barcode
import com.simonsickle.compose.barcodes.BarcodeType

@Composable
fun TrackPreviewBottomSheet(
    musicModel: PreviewTrackModel?,
    viewModel: MediaPlayerViewModel
) {
    val rotation = remember { Animatable(0f) }
    var isRotating by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()


    val context = LocalContext.current
    DisposableEffect(Unit) {
        val window = (context as? Activity)?.window
        window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        onDispose {
            window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }
    musicModel?.let {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .aspectRatio(1f)
                        .clip(CircleShape)
                        .clickable {
                            if (isRotating) {
                                viewModel.pauseMediaPlayer()
                            } else {
                                viewModel.resumeMediaPlayer()
                            }
                            isRotating = !isRotating
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Box(modifier = Modifier.rotate(rotation.value)) {
                        Image(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .aspectRatio(1f),
                            painter = painterResource(R.drawable.vinyl_image),
                            contentDescription = ""
                        )
                        AsyncImage(
                            model = "https://cdn-images.dzcdn.net/images/cover/${musicModel?.cover}/200x200.jpg",
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .fillMaxWidth(0.4f)
                                .aspectRatio(1f)

                                .clip(CircleShape)
                        )
                    }


                    Box(

                        modifier = Modifier
                            .size(44.dp)
                            .align(Alignment.Center)
                            .clip(CircleShape)
                            .background(Color.White)

                    )
                    Image(
                        painter = painterResource(
                            if (isRotating) R.drawable.ic_pause else R.drawable.ic_play
                        ), colorFilter = ColorFilter.tint(Color.Black),
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp)
                            .align(Alignment.Center)

                    )
                }
                Image(
                    modifier = Modifier
                        .fillMaxWidth(0.2f)
                        .align(Alignment.TopEnd),
                    painter = painterResource(R.drawable.turntable_image),
                    contentDescription = ""
                )
            }


            Text(
                modifier = Modifier.fillMaxWidth(),
                text = musicModel?.title.orEmpty(),
                fontWeight = FontWeight.Bold,
                style = TextStyle(fontSize = 26.sp)
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = musicModel?.album.orEmpty(),
                fontWeight = FontWeight.Medium,
                style = TextStyle(fontSize = 22.sp)
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = musicModel?.artistName.orEmpty(),
                fontWeight = FontWeight.Normal,
                style = TextStyle(fontSize = 20.sp, color = Color.Gray)
            )
        }
    }


    LaunchedEffect(musicModel) {
        musicModel?.preview?.let { previewUrl ->
            viewModel.startMediaPlayer(previewUrl)
            isRotating = true
        }
    }

    LaunchedEffect(isRotating) {
        if (isRotating) {
            rotation.animateTo(
                targetValue = 360f + rotation.value,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 5000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                )
            )
        } else {
            rotation.stop()
        }
    }
}

fun setScreenBrightness(activity: Activity, brightness: Float) {
    val window = activity.window
    val layoutParams = window.attributes
    layoutParams.screenBrightness = brightness
    window.attributes = layoutParams
}

@Composable
fun BarcodeBottomSheet(
    barcode: String?,
) {

    val context = LocalContext.current
    DisposableEffect(Unit) {
        val window = (context as? Activity)?.window
        window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setScreenBrightness(context as Activity, 1.0f)
        onDispose {
            window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            setScreenBrightness(
                context as Activity,
                WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
            )

        }
    }

    Barcode(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth(),
        resolutionFactor = 10,
        type = BarcodeType.EAN_13,
        value = barcode
            ?: ""
    )

}
