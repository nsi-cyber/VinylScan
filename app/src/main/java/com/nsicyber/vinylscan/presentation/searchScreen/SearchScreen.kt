package com.nsicyber.vinylscan.presentation.searchScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.nsicyber.vinylscan.R
import com.nsicyber.vinylscan.data.model.response.discogs.getSearch.SearchResultItem
import com.nsicyber.vinylscan.domain.model.VinylModel
import com.nsicyber.vinylscan.presentation.components.BaseView
import com.nsicyber.vinylscan.presentation.components.SearchCard
import com.nsicyber.vinylscan.presentation.components.SearchInputField


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    searchScreenViewModel: SearchScreenViewModel = hiltViewModel(),
    onBackPressed: () -> Unit,
    onDetail: (data: VinylModel?) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val searchScreenState by searchScreenViewModel.uiState.collectAsState()
    val context = LocalContext.current


    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        snapshotFlow { focusRequester }
            .collect {
                focusRequester.requestFocus()
            }
    }

    LaunchedEffect(searchScreenState.onSuccess) {
        if (searchScreenState.onSuccess && searchScreenState.vinylModel != null) {
            onDetail(searchScreenState.vinylModel)
            searchScreenViewModel.onEvent(SearchScreenEvent.SetStateEmpty)
        }
    }

    BaseView(isPageLoading = searchScreenState.isPageLoading,


        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.SpaceBetween) {
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
                    SearchInputField(
                        focusRequester = focusRequester,
                        onQueryChange = { searchScreenViewModel.onEvent(SearchScreenEvent.Search(it)) },
                        onClearQuery = { searchScreenViewModel.onEvent(SearchScreenEvent.SetStateEmpty) },
                        query = searchScreenState.searchQuery
                    )
                }

                LazyColumn(modifier = Modifier.fillMaxSize(1f), contentPadding = PaddingValues(vertical = 32.dp)) {
                    itemsIndexed(
                        searchScreenState.searchSearchResultItem ?: listOf()
                    ) { index, data ->
                        SearchCard(
                            data,
                            onItemClick = {
                                searchScreenViewModel.onEvent(
                                    SearchScreenEvent.OpenDetail(index)
                                )
                            })
                    }
                }
            }


        }
    )
}