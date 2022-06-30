package com.krykun.movieapp.feature.discover.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.krykun.domain.model.MovieDiscoverItem
import com.krykun.movieapp.feature.discover.presentation.DiscoverMoviesViewModel

@Composable
fun DiscoverView(viewModel: DiscoverMoviesViewModel) {

    val movies = viewModel.getDiscoverMovies.collectAsLazyPagingItems()

    val lazyListState = rememberLazyListState()

    Column(
        modifier = Modifier
            .background(Color.Transparent)
            .fillMaxSize()
    ) {
        LazyColumn(
            state = lazyListState,
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
        ) {
            items(
                count = movies.itemCount,
            ) { index ->
                movies[index]?.let { DiscoverMoviesItem(moviesItem = it) }
            }
        }
    }
    DisposableEffect(key1 = true) {
        onDispose {
            viewModel.scrollIndex.value = lazyListState.firstVisibleItemIndex
            viewModel.scrollOffset.value = lazyListState.firstVisibleItemScrollOffset
        }
    }
    LaunchedEffect(key1 = true) {
        lazyListState.scrollToItem(viewModel.scrollIndex.value, viewModel.scrollOffset.value)
    }
}

@Composable
private fun DiscoverMoviesItem(moviesItem: MovieDiscoverItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
    ) {
        moviesItem.title?.let { Text(text = it) }
    }
}