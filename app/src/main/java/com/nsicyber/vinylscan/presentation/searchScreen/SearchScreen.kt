package com.nsicyber.vinylscan.presentation.searchScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nsicyber.vinylscan.R
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
            searchScreenViewModel.onEvent(SearchScreenEvent.DetailOpened)
        }
    }

    BaseView(isPageLoading = searchScreenState.isPageLoading,

        modifier = Modifier.background(Color.Black),

        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.Top,
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
                    SearchInputField(
                        focusRequester = focusRequester,
                        onQueryChange = { searchScreenViewModel.onEvent(SearchScreenEvent.Search(it)) },
                        onClearQuery = { searchScreenViewModel.onEvent(SearchScreenEvent.SetStateEmpty) },
                        query = searchScreenState.searchQuery
                    )
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(1f),
                    contentPadding = PaddingValues(vertical = 32.dp)
                ) {

                    if (searchScreenState.searchSearchResultItem.isNullOrEmpty() &&
                        !searchScreenState.searchQuery.isNullOrBlank() &&
                        searchScreenState.isPageLoading == false
                    ) {
                        item {
                            Text(text = stringResource(R.string.no_result_found))
                        }
                    }

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