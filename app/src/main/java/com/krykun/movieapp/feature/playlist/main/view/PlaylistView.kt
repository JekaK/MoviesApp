package com.krykun.movieapp.feature.playlist.main.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.krykun.domain.model.local.Playlist
import com.krykun.movieapp.ext.noRippleClickable
import com.krykun.movieapp.feature.playlist.main.presentation.PlaylistSideEffects
import com.krykun.movieapp.feature.playlist.main.presentation.PlaylistViewModel
import com.krykun.movieapp.navigation.Screen
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun PlaylistView(
    viewModel: PlaylistViewModel = hiltViewModel(),
    navHostController: NavHostController
) {
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
    if (allPlaylists.value.isNotEmpty()) {
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = lazyListState
            ) {
                itemsIndexed(items = allPlaylists.value) { index, item ->
                    if (item.movieList.isNotEmpty()) {
                        PlaylistItemView(playlist = item, modifier = Modifier.noRippleClickable {
                            viewModel.navigateToPlaylistDetails(item.playlistId)
                        })
                    }
                }
            }
        }
    } else {
        EmptyView()
    }

    viewModel.collectSideEffect {
        handleSideEffects(
            it,
            navHostController
        )
    }
}

fun handleSideEffects(
    sideEffects: PlaylistSideEffects,
    navHostController: NavHostController
) {
    when (sideEffects) {
        PlaylistSideEffects.NavigateToPlaylistDetails -> {
            navHostController.navigate(Screen.PlaylistDetails().route)
        }
    }
}
