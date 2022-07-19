package com.krykun.movieapp.feature.playlist.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.krykun.movieapp.feature.playlist.presentation.PlaylistViewModel

@Composable
fun PlaylistView(viewModel: PlaylistViewModel) {
    val lazyListState = rememberLazyListState()
    val allPlaylists = viewModel.allPlaylists.collectAsState(initial = emptyList())
    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = lazyListState
        ) {
            itemsIndexed(items = allPlaylists.value) { index, item ->
                PlaylistItemView(item)
            }
        }
    }
}