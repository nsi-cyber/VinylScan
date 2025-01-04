package com.nsicyber.vinylscan.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.nsicyber.vinylscan.R
import com.nsicyber.vinylscan.common.formatDate
import com.nsicyber.vinylscan.common.formatDuration
import com.nsicyber.vinylscan.data.model.response.deezer.getAlbumDetail.GetAlbumDetailResponse
import com.nsicyber.vinylscan.data.model.response.deezer.getAlbumDetail.TrackDetail
import com.nsicyber.vinylscan.presentation.MediaPlayerViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeezerAlbumDetailBottomSheet(
    data: GetAlbumDetailResponse?,
    viewModel: MediaPlayerViewModel = hiltViewModel<MediaPlayerViewModel>()

) {

    val bottomSheetState: BottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.Hidden,
            skipHiddenState = false
        ),
    )

    val scope = rememberCoroutineScope()
    val bottomSheetData = remember { mutableStateOf<TrackDetail?>(null) }


    LaunchedEffect(bottomSheetState.bottomSheetState.currentValue) {
        if (bottomSheetState.bottomSheetState.currentValue == SheetValue.PartiallyExpanded) {
            //set stop
            viewModel.stopMediaPlayer()
            bottomSheetState.bottomSheetState.hide()
        }
    }



    BaseView(
        bottomSheetState = bottomSheetState,
        bottomSheetContent = {
            TrackPreviewBottomSheet(
                bottomSheetData.value, viewModel
            )
        },

        content = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                data?.cover_xl?.takeIf { !it.isNullOrBlank() }?.let {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f),
                        model = data?.cover_xl,
                        contentDescription = ""
                    )
                }


                Column(modifier = Modifier.padding(start = 16.dp)) {
                    data?.title?.takeIf { !it.isNullOrBlank() }?.let {
                        Text(
                            text = it,
                            color = Color.White,
                            fontSize = 28.sp,
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    data?.artist?.name?.takeIf { !it.isNullOrBlank() }?.let {
                        Text(
                            lineHeight = 24.sp,
                            text = it,
                            color = Color.Gray,
                            fontSize = 22.sp,
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                data?.release_date?.takeIf { !it.isNullOrBlank() }?.let {

                    Text(
                        modifier = Modifier.fillMaxWidth().padding(start = 16.dp),
                        text = formatDate(it),
                        color = Color.White,
                        fontSize = 28.sp,
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.Bold,
                    )
                }
                data?.genres?.data?.takeIf { !it.isNullOrEmpty() }?.let {

                    Text(
                        text = it?.map { it?.name }?.joinToString(", ").orEmpty(),
                        color = Color.White,
                        fontSize = 28.sp,
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth().padding(start = 16.dp),
                    )

                }
                data?.tracks?.data?.takeIf { !it.isNullOrEmpty() }?.let {

                    repeat(it?.size ?: 0) { trackIndex ->
                        Row(modifier = Modifier
                            .clickable {

                                bottomSheetData.value = it?.get(trackIndex)
                                scope.launch {
                                    bottomSheetState.bottomSheetState.expand()
                                }

                            }
                            .padding(start = 16.dp)
                        ) {
                            Image(
                                modifier = Modifier.size(32.dp),
                                painter = painterResource(R.drawable.ic_play),
                                colorFilter = ColorFilter.tint(Color.White), contentDescription = ""
                            )
                            Column(
                                modifier = Modifier
                            ) {
                                Text(
                                    text = it?.get(trackIndex)?.title.orEmpty(),
                                    color = Color.White,
                                    fontSize = 22.sp,
                                    textAlign = TextAlign.Start,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Text(
                                    lineHeight = 24.sp,
                                    text = formatDuration(it?.get(trackIndex)?.duration),
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
            }
        }
    )


}

