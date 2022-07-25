package com.krykun.movieapp.feature.playlistselect.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.krykun.movieapp.feature.playlistselect.presentation.PlaylistSelectSideEffects
import com.krykun.movieapp.feature.playlistselect.presentation.PlaylistSelectViewModel
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun PlaylistSelectedView(viewModel: PlaylistSelectViewModel = hiltViewModel()) {

    Column(
        modifier = Modifier.padding(8.dp),
    ) {
        Text(text = "Simple Dialog", style = MaterialTheme.typography.h6)
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            content = {
                itemsIndexed(items = viewModel.playlistState.value) { index, item ->
                    Row(modifier = Modifier.clickable {
                        viewModel.addMovieToPlaylist(playlistId = item.playlist.playlistId)
                    }) {
                        Text(text = item.playlist.name)
                        Icon(
                            imageVector = if (item.isMovieInPlaylist) {
                                Icons.Default.AddCircle
                            } else {
                                Icons.Default.Remove
                            }, contentDescription = ""
                        )
                    }
                }
            }
        )
        Button(onClick = { }) {
            Text("Cancel")
        }
    }
    viewModel.collectSideEffect {
        handleSideEffects(
            sideEffects = it,
            viewModel = viewModel
        )
    }
}

private fun handleSideEffects(
    sideEffects: PlaylistSelectSideEffects,
    viewModel: PlaylistSelectViewModel
) {
    when (sideEffects) {
        is PlaylistSelectSideEffects.UpdatePlaylistSelectList -> {
            viewModel.playlistState.value = sideEffects.playlists
        }
    }
}
