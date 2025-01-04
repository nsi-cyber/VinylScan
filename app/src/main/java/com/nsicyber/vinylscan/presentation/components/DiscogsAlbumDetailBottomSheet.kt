package com.nsicyber.vinylscan.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.nsicyber.vinylscan.data.model.response.discogs.getDetail.GetDetailResponse


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscogsAlbumDetailBottomSheet(
    data: GetDetailResponse?,
) {

    val bottomSheetState: BottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.Hidden,
            skipHiddenState = false
        ),
    )

    LaunchedEffect(bottomSheetState.bottomSheetState.currentValue) {
        if (bottomSheetState.bottomSheetState.currentValue == SheetValue.PartiallyExpanded) {
            bottomSheetState.bottomSheetState.hide()
        }
    }



    BaseView(

        content = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                data?.thumbnail?.takeIf { !it.isNullOrBlank() }?.let {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f),
                        model = it,
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
                    data?.artists?.firstOrNull()?.name?.takeIf { !it.isNullOrBlank() }?.let {
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
                data?.year?.takeIf { it != null }?.let {
                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        text = it.toString(),
                        color = Color.White,
                        fontSize = 28.sp,
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.Bold,
                    )
                }


                data?.genres?.takeIf { !it.isNullOrEmpty() }?.let {
                    Text(
                        text = it?.joinToString(", ").orEmpty(),
                        color = Color.White,
                        fontSize = 28.sp,
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth().padding(start = 16.dp),
                    )
                }

                data?.styles?.takeIf { !it.isNullOrEmpty() }?.let {

                    Text(
                        text = it?.joinToString(", ").orEmpty(),
                        color = Color.Gray,
                        fontSize = 22.sp,
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.fillMaxWidth().padding(start = 16.dp),
                    )
                }
                data?.tracklist?.takeIf { !it.isNullOrEmpty() }?.let {

                    repeat(it?.size ?: 0) { trackIndex ->
                        Row(
                            modifier = Modifier
                                .padding(start = 16.dp)
                        ) {

                            Column(
                                modifier = Modifier
                            ) {
                                Text(
                                    text = data?.tracklist?.get(trackIndex)?.title.orEmpty(),
                                    color = Color.White,
                                    fontSize = 22.sp,
                                    textAlign = TextAlign.Start,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Text(
                                    lineHeight = 24.sp,
                                    text = data?.tracklist?.get(trackIndex)?.duration.orEmpty(),
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
