package com.nsicyber.vinylscan.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.nsicyber.vinylscan.common.preloadImages
import kotlinx.coroutines.launch


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
                        val targetAngle = when {
                            flipAngle.value > 90 -> 180f
                            flipAngle.value < -90 -> -180f
                            else -> 0f
                        }

                        scope.launch {
                            flipAngle.animateTo(
                                targetValue = targetAngle,
                                animationSpec = tween(
                                    durationMillis = 300,
                                    easing = FastOutSlowInEasing
                                )
                            )

                            if (targetAngle == 180f) {
                                currentIndex = (currentIndex + 1) % imageUrls.size
                            } else if (targetAngle == -180f) {
                                currentIndex = (currentIndex - 1 + imageUrls.size) % imageUrls.size
                            }
                            flipAngle.snapTo(0f)
                        }
                    },
                    onHorizontalDrag = { _, dragAmount ->
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
                AsyncImage(
                    model = imageUrls[currentIndex],
                    contentDescription = "Image $currentIndex",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
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