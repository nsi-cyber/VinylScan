package com.nsicyber.vinylscan.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.nsicyber.vinylscan.data.model.response.discogs.getSearch.SearchResultItem

@Composable
fun SearchCard(
    data: SearchResultItem?, onItemClick: (data: SearchResultItem?) -> Unit = {}
) {

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable {
                onItemClick(data)
            }
            .height(100.dp)
            .fillMaxWidth()
            .padding(vertical = 8.dp)


    ) {


        AsyncImage(
            model = data?.cover_image,
            contentDescription = data?.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .aspectRatio(1f)
                .shadow(5.dp, RoundedCornerShape(10.dp))
                .clip(
                    RoundedCornerShape(10.dp)
                )
                .background(Color.Gray)

        )
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(start = 8.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = data?.title.orEmpty(),
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontSize = 18.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Medium,
            )
            Text(
                text = data?.year.orEmpty(),
                color = Color.Gray,
                fontSize = 12.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
            )
        }


    }

}