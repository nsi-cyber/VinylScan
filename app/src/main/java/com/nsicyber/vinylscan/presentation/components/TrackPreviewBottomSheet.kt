package com.nsicyber.vinylscan.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.nsicyber.vinylscan.R
import com.nsicyber.vinylscan.data.model.response.deezer.getAlbumDetail.TrackDetail
import com.nsicyber.vinylscan.presentation.MediaPlayerViewModel

@Composable
fun TrackPreviewBottomSheet(
    musicModel: TrackDetail?,
    viewModel: MediaPlayerViewModel
) {
    val rotation = remember { Animatable(0f) }
    var isRotating by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()




    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(180.dp)
                .padding(top = 24.dp)
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
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(musicModel?.album?.cover_xl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(180.dp)
                    .aspectRatio(1f)
                    .rotate(rotation.value)
                    .clip(RoundedCornerShape(99.dp))
            )

            Image(
                painter = painterResource(
                    if (isRotating) R.drawable.ic_pause else R.drawable.ic_play
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.Center)

            )
        }

        Text(
            text = musicModel?.title.orEmpty(),
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(vertical = 14.dp)
        )
        Text(
            text = musicModel?.album?.title.orEmpty(),
            fontSize = 12.sp,
            fontWeight = FontWeight.Light,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = musicModel?.artist?.name.orEmpty(),
            modifier = Modifier.padding(bottom = 40.dp)
        )
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
