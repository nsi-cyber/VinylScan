package com.nsicyber.vinylscan.presentation.favoriteScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.nsicyber.vinylscan.R
import com.nsicyber.vinylscan.domain.model.VinylModel
import com.nsicyber.vinylscan.presentation.components.BaseView
import com.nsicyber.vinylscan.presentation.components.EmptyView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(
    favoriteViewModel: FavoriteViewModel = hiltViewModel(),
    onDetail: (data: VinylModel?) -> Unit,
    onBackPressed: () -> Unit
) {
    val favoriteState by favoriteViewModel.favoriteScreenState.collectAsState()

    LaunchedEffect(Unit) {
        favoriteViewModel.onEvent(FavoriteScreenEvent.LoadScreen)
    }

    LaunchedEffect(favoriteState.onSuccess) {
        if (favoriteState.onSuccess && favoriteState.vinylModel != null) {
            onDetail(favoriteState.vinylModel)
            favoriteViewModel.onEvent(FavoriteScreenEvent.DetailOpened)
        }
    }



    BaseView(
        isPageLoading = favoriteState.isPageLoading,
        modifier = Modifier.background(Color.Black),
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .padding(end = 8.dp)

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
                    Text(
                        text = stringResource(R.string.favorites),
                        color = Color.White,
                        fontSize = 32.sp,
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.Normal,
                        lineHeight = 32.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }


                if (favoriteState.favoriteVinyls.isNullOrEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        EmptyView(
                            text = stringResource(R.string.no_favorites_yet),
                            icon = R.drawable.ic_empty
                        )
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(vertical = 16.dp)
                    ) {
                        items(favoriteState.favoriteVinyls ?: emptyList()) { vinyl ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(20.dp))
                                    .clickable {
                                        vinyl?.vinylId?.let {
                                            favoriteViewModel.onEvent(
                                                FavoriteScreenEvent.OpenDetail(
                                                    it
                                                )
                                            )
                                        }
                                    }
                                    .background(Color.White.copy(alpha = 0.1f))
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(vinyl?.image)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                )

                                Column {
                                    Text(
                                        text = vinyl?.title ?: "",
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Normal
                                    )
                                    Text(
                                        text = vinyl?.releaseDate ?: "",
                                        color = Color.Gray,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    )

} 