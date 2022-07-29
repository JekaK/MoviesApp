package com.krykun.movieapp.feature.playlist.main.view

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.krykun.domain.model.local.Playlist
import com.krykun.movieapp.R
import com.krykun.movieapp.ext.noRippleLongClickable
import com.krykun.movieapp.feature.playlist.main.presentation.PlaylistSideEffects
import com.krykun.movieapp.feature.playlist.main.presentation.PlaylistViewModel
import com.krykun.movieapp.navigation.Screen
import org.orbitmvi.orbit.compose.collectSideEffect

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlaylistView(
    viewModel: PlaylistViewModel = hiltViewModel(),
    navHostController: NavHostController,
    innerPadding: PaddingValues
) {
    val lazyListState = rememberLazyListState()
    val scaffoldState = rememberScaffoldState()
    var showPlaylistCreationDialog by remember {
        mutableStateOf(false)
    }

    var showPlaylistDeleteDialog by remember {
        mutableStateOf(false)
    }
    var selectedPlaylistItem by remember {
        mutableStateOf(Playlist())
    }

    if (showPlaylistDeleteDialog) {
        DeletePlaylistConfirmationView(
            onApply = {
                viewModel.removePlaylist(it)
                showPlaylistDeleteDialog = false
            },
            onDismiss = {
                showPlaylistDeleteDialog = false
            },
            item = selectedPlaylistItem
        )
    }


    if (showPlaylistCreationDialog) {
        CreatePlaylistView(
            onApply = {
                viewModel.addPlaylist(it)
                showPlaylistCreationDialog = false
            },
            onDismiss = {
                showPlaylistCreationDialog = false
            })
    }

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding()),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showPlaylistCreationDialog = true
                },
                shape = RoundedCornerShape(20.dp),
                contentColor = colorResource(id = R.color.floating_button_color)
            ) {
                Icon(
                    imageVector = Icons.Default.Add, contentDescription = "",
                    tint = Color.White
                )
            }
        },
        backgroundColor = Color.Transparent,
    ) {
        if (viewModel.playlistState.value.isNotEmpty()) {
            Column(modifier = Modifier.fillMaxSize()) {
                CompositionLocalProvider(
                    LocalOverscrollConfiguration provides null
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize(),
                        state = lazyListState
                    ) {
                        itemsIndexed(items = viewModel.playlistState.value) { index, item ->
                            PlaylistItemView(
                                playlist = item,
                                modifier = Modifier.noRippleLongClickable(onLongClick = {
                                    selectedPlaylistItem = item
                                    showPlaylistDeleteDialog = true
                                }, onClick = {
                                    viewModel.navigateToPlaylistDetails(item.playlistId)
                                })
                            )
                        }
                    }
                }
            }
        } else {
            EmptyView()
        }
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
