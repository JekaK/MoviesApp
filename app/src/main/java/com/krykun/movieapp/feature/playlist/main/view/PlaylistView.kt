package com.krykun.movieapp.feature.playlist.main.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.krykun.movieapp.ext.noRippleClickable
import com.krykun.movieapp.feature.playlist.main.presentation.PlaylistSideEffects
import com.krykun.movieapp.feature.playlist.main.presentation.PlaylistViewModel
import com.krykun.movieapp.navigation.Screen
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlaylistView(
    viewModel: PlaylistViewModel = hiltViewModel(),
    navHostController: NavHostController,
    innerPadding: PaddingValues
) {
    val lazyListState = rememberLazyListState()

    if (viewModel.playlistState.value.isNotEmpty()) {
        Column(modifier = Modifier.fillMaxSize()) {
            CompositionLocalProvider(
                LocalOverscrollConfiguration provides null
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = innerPadding.calculateBottomPadding()),
                    state = lazyListState
                ) {
                    itemsIndexed(items = viewModel.playlistState.value) { index, item ->
                        if (item.movieList.isNotEmpty()) {
                            PlaylistItemView(
                                playlist = item,
                                modifier = Modifier.noRippleClickable {
                                    viewModel.navigateToPlaylistDetails(item.playlistId)
                                })
                        }
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
            navHostController,
            viewModel
        )
    }
}

private fun handleSideEffects(
    sideEffects: PlaylistSideEffects,
    navHostController: NavHostController,
    viewModel: PlaylistViewModel
) {
    when (sideEffects) {
        PlaylistSideEffects.NavigateToPlaylistDetails -> {
            navHostController.navigate(Screen.PlaylistDetails().route)
        }
        is PlaylistSideEffects.UpdatePlaylist -> {
            viewModel.playlistState.value = sideEffects.playlist
        }
    }
}
