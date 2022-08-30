package com.krykun.movieapp.feature.addtoplaylist.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.krykun.data.util.Constants
import com.krykun.movieapp.R
import com.krykun.movieapp.ext.header
import com.krykun.movieapp.feature.playlist.main.view.CreatePlaylistView
import com.krykun.movieapp.feature.addtoplaylist.presentation.MappedPlaylist
import com.krykun.movieapp.feature.addtoplaylist.presentation.PlaylistSelectSideEffects
import com.krykun.movieapp.feature.addtoplaylist.presentation.PlaylistSelectViewModel
import com.skydoves.landscapist.coil.CoilImage
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun PlaylistSelectedView(viewModel: PlaylistSelectViewModel = hiltViewModel()) {

    var showPlaylistCreationDialog by remember {
        mutableStateOf(false)
    }
    if (showPlaylistCreationDialog) {
        CreatePlaylistView(
            onApply = {
                viewModel.addPlaylist(it)
                viewModel.updateAllPlaylists()
                showPlaylistCreationDialog = false
            },
            onDismiss = {
                showPlaylistCreationDialog = false
            })
    }
    Box(modifier = Modifier.padding(16.dp)) {
        Column(
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            Text(
                text = stringResource(R.string.select_playlist_for),
                style = MaterialTheme.typography.h6,
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = 16.dp,
                        bottom = 16.dp
                    ),
                content = {
                    itemsIndexed(items = viewModel.playlistState.value) { index, item ->
                        PlaylistItemView(viewModel, item, this@Column)
                    }
                }
            )
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 8.dp,
                    bottom = 8.dp
                )
                .align(Alignment.BottomCenter),
            elevation = 16.dp,
            backgroundColor = colorResource(id = R.color.purple_500),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text(
                text = "+ Add playlist",
                color = Color.White,
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 8.dp,
                        bottom = 8.dp
                    )
                    .clickable {
                        showPlaylistCreationDialog = true
                    }
            )
        }
    }
    viewModel.collectSideEffect {
        handleSideEffects(
            sideEffects = it,
            viewModel = viewModel
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun PlaylistItemView(
    viewModel: PlaylistSelectViewModel,
    item: MappedPlaylist,
    columnScope: ColumnScope
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 8.dp,
                bottom = 8.dp
            ),
        elevation = 16.dp,
        shape = RoundedCornerShape(20.dp),
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Row(modifier = Modifier
                .clickable {
                    viewModel.changeMoviePlaylistStatus(playlistId = item.playlist.playlistId)
                }
                .padding(
                    start = 8.dp,
                    top = 8.dp,
                    bottom = 8.dp,
                    end = 16.dp
                )
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically) {
                ImageCollageView(
                    item.playlist.movieList.map {
                        it.poster
                    })
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = item.playlist.name,
                    maxLines = 1,
                    modifier = Modifier.padding(end = 32.dp),
                    overflow = TextOverflow.Ellipsis
                )
            }
            columnScope.AnimatedVisibility(
                visible = item.isMovieInPlaylist,
                enter = scaleIn(),
                exit = scaleOut(),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp),
            ) {
                Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = "",
                    modifier = Modifier
                        .size(24.dp)
                )
            }

        }
    }
}

@Composable
private fun ImageCollageView(imageList: List<String>) {
    Card(
        modifier = Modifier.size(width = 50.dp, height = 75.dp),
        elevation = 16.dp,
        shape = RoundedCornerShape(10.dp)
    ) {
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            columns = GridCells.Adaptive(minSize = 25.dp),
        ) {
            if (imageList.size == 1) {
                header {
                    CoilImage(
                        imageModel = Constants.IMAGE_BASE_URL + imageList.first(),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                        placeHolder = ImageVector.vectorResource(id = R.drawable.ic_movie_placeholder),
                        error = ImageVector.vectorResource(id = R.drawable.ic_movie_placeholder),
                        modifier = Modifier.size(75.dp)
                    )
                }
            }

            if (imageList.size == 3) {
                items(count = 2) { index ->
                    CoilImage(
                        imageModel = Constants.IMAGE_BASE_URL + imageList[index],
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                        placeHolder = ImageVector.vectorResource(id = R.drawable.ic_movie_placeholder),
                        error = ImageVector.vectorResource(id = R.drawable.ic_movie_placeholder),
                        modifier = Modifier.size(width = 25.dp, height = 37.5.dp)
                    )
                }
                header {
                    CoilImage(
                        imageModel = Constants.IMAGE_BASE_URL + imageList[2],
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        placeHolder = ImageVector.vectorResource(id = R.drawable.ic_movie_placeholder),
                        error = ImageVector.vectorResource(id = R.drawable.ic_movie_placeholder),
                        modifier = Modifier
                            .height(37.5.dp)
                            .fillMaxWidth()
                    )
                }
            }

            if (imageList.size == 2 || imageList.size == 4) {
                items(count = imageList.size) { index ->
                    CoilImage(
                        imageModel = Constants.IMAGE_BASE_URL + imageList[index],
                        contentDescription = null,
                        contentScale = if (imageList.size == 2) {
                            ContentScale.FillHeight
                        } else {
                            ContentScale.FillBounds
                        },
                        placeHolder = ImageVector.vectorResource(id = R.drawable.ic_movie_placeholder),
                        error = ImageVector.vectorResource(id = R.drawable.ic_movie_placeholder),
                        modifier = Modifier.size(
                            width = 25.dp, height = if (imageList.size == 2) {
                                75.dp
                            } else {
                                37.5.dp
                            }
                        )
                    )
                }
            }
        }
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
