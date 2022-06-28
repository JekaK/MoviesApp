package com.krykun.movieapp.feature.discovermovies.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.krykun.domain.model.MovieDiscoverItem

@Composable
fun DiscoverMoviesContent(allMovies: LazyPagingItems<MovieDiscoverItem>) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
    ) {
        items(
            count = allMovies.itemCount,
        ) { index ->
            allMovies[index]?.let { DiscoverMoviesItem(moviesItem = it) }
        }
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