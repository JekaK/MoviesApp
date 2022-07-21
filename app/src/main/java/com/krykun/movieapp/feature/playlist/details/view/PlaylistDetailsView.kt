package com.krykun.movieapp.feature.playlist.details.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.krykun.data.util.Constants
import com.krykun.domain.model.local.Movie
import com.krykun.movieapp.R
import com.krykun.movieapp.ext.header
import com.krykun.movieapp.ext.noRippleClickable
import com.krykun.movieapp.feature.playlist.details.presentation.PlaylistDetailsSideEffects
import com.krykun.movieapp.feature.playlist.details.presentation.PlaylistDetailsViewModel
import com.krykun.movieapp.navigation.Screen
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.coil.CoilImage
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlaylistDetailsView(
    viewModel: PlaylistDetailsViewModel = hiltViewModel(),
    navHostController: NavHostController
) {
    CompositionLocalProvider(
        LocalOverscrollConfiguration provides null
    ) {
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            columns = GridCells.Adaptive(minSize = 128.dp),
        ) {
            header {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = viewModel.playlistInfo.value.name,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        modifier = Modifier.padding(
                            top = 24.dp,
                            bottom = 24.dp
                        )
                    )
                }
            }
            itemsIndexed(items = viewModel.playlistInfo.value.movieList) { index, item ->
                when (item.type) {
                    "movie" -> MovieView(modifier = Modifier.noRippleClickable {
                        viewModel.navigateToMovieDetails(item.movieId.toInt())
                    }, movie = item)
                    "tvseries" -> TvSeriesView(modifier = Modifier.noRippleClickable {
                        viewModel.navigateToTvDetailsDetails(item.movieId.toInt())
                    }, movie = item)
                }
            }
        }
    }
    viewModel.collectSideEffect {
        handleSideEffects(
            sideEffects = it,
            viewModel,
            navHostController = navHostController
        )
    }
}

@Composable
fun MovieView(
    modifier: Modifier = Modifier,
    movie: Movie
) {
    Box(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(200.dp)
            .background(colorResource(id = R.color.container_background))
            .clip(RoundedCornerShape(20.dp))
    ) {
        CoilImage(
            imageModel = Constants.IMAGE_BASE_URL + movie.poster,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            circularReveal = CircularReveal(duration = 350),
            placeHolder = ImageVector.vectorResource(id = R.drawable.ic_movie_placeholder),
            error = ImageVector.vectorResource(id = R.drawable.ic_movie_placeholder),
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(alignment = Alignment.BottomCenter)
                .background(colorResource(id = R.color.bottom_bar_start))
        ) {
            Text(
                text = movie.name,
                color = Color.White,
                modifier = Modifier.padding(
                    start = 12.dp,
                    end = 12.dp,
                    top = 6.dp,
                    bottom = 15.dp
                ),
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun TvSeriesView(
    modifier: Modifier = Modifier,
    movie: Movie
) {
    Box(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(200.dp)
            .background(colorResource(id = R.color.container_background))
            .clip(RoundedCornerShape(20.dp))
    ) {
        CoilImage(
            imageModel = Constants.IMAGE_BASE_URL + movie.poster,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            circularReveal = CircularReveal(duration = 350),
            placeHolder = ImageVector.vectorResource(id = R.drawable.ic_movie_placeholder),
            error = ImageVector.vectorResource(id = R.drawable.ic_movie_placeholder),
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(alignment = Alignment.BottomCenter)
                .background(colorResource(id = R.color.bottom_bar_start))
        ) {
            Text(
                text = movie.name,
                color = Color.White,
                modifier = Modifier.padding(
                    start = 12.dp,
                    end = 12.dp,
                    top = 6.dp,
                    bottom = 15.dp
                ),
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

private fun handleSideEffects(
    sideEffects: PlaylistDetailsSideEffects,
    viewModel: PlaylistDetailsViewModel,
    navHostController: NavHostController
) {
    when (sideEffects) {
        is PlaylistDetailsSideEffects.UpdatePlaylistInfo -> {
            viewModel.playlistInfo.value = sideEffects.playlist
        }
        is PlaylistDetailsSideEffects.NavigateToMovieDetails -> {
            navHostController.navigate(Screen.MovieDetails().route)
        }
        is PlaylistDetailsSideEffects.NavigateToTvDetails -> {
            navHostController.navigate(Screen.TvSeriesDetails().route)
        }
    }
}
