package com.krykun.movieapp.feature.playlist.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.krykun.domain.model.local.Playlist
import com.krykun.movieapp.feature.playlist.presentation.PlaylistViewModel
import kotlinx.coroutines.flow.collect

@Composable
fun PlaylistView(viewModel: PlaylistViewModel = hiltViewModel()) {
    val lazyListState = rememberLazyListState()
    val allPlaylists = remember {
        mutableStateOf(listOf<Playlist>())
    }
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(key1 = lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.subscribeToState()
                .collect {
                    allPlaylists.value = it.playlists
                }
        }
    }

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