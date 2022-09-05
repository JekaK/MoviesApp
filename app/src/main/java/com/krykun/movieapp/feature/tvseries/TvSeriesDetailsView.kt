package com.krykun.movieapp.feature.tvseries

import androidx.compose.animation.Crossfade
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.krykun.domain.model.remote.tvdetails.TvDetails
import com.krykun.movieapp.feature.addtoplaylist.presentation.PlaylistSelectViewModel
import com.krykun.movieapp.feature.tvseries.presentation.TvSeriesDetailsSideEffects
import com.krykun.movieapp.feature.tvseries.presentation.TvSeriesDetailsViewModel
import com.krykun.movieapp.feature.tvseries.presentation.TvSeriesDetailsViewModel.MovieDetailsState
import com.krykun.movieapp.feature.tvseries.view.ErrorView
import com.krykun.movieapp.feature.tvseries.view.LoadingView
import com.krykun.movieapp.feature.tvseries.view.TvSeriesDetailsBasicView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalMaterialApi::class)
@ExperimentalMotionApi
@Composable
fun TvSeriesDetailsView(
    viewModel: TvSeriesDetailsViewModel = hiltViewModel(),
    playlistSelectViewModel: PlaylistSelectViewModel = hiltViewModel(),
    navHostController: NavHostController
) {
    val scope = rememberCoroutineScope()

    val bottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    Crossfade(targetState = viewModel.movieDetailsState.value) {
        when (it) {
            MovieDetailsState.LOADING -> {
                LoadingView()
            }
            MovieDetailsState.DEFAULT -> {
                TvSeriesDetailsBasicView(
                    navHostController = navHostController,
                    viewModel = viewModel,
                    bottomSheetState = bottomSheetState,
                    playlistSelectViewModel = playlistSelectViewModel
                )
            }
            MovieDetailsState.ERROR -> {
                ErrorView()
            }
        }
    }

    viewModel.collectSideEffect {
        handleSideEffects(
            sideEffects = it,
            movieData = viewModel.movieData,
            isRatingVisible = viewModel.isRatingVisible,
            movieDetailsState = viewModel.movieDetailsState,
            scope = scope,
            bottomSheetState = bottomSheetState,
            playlistSelectViewModel = playlistSelectViewModel
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
private fun handleSideEffects(
    sideEffects: TvSeriesDetailsSideEffects,
    movieData: MutableState<TvDetails?>,
    isRatingVisible: MutableState<Boolean>,
    movieDetailsState: MutableState<MovieDetailsState>,
    scope: CoroutineScope,
    bottomSheetState: ModalBottomSheetState,
    playlistSelectViewModel: PlaylistSelectViewModel
) {
    when (sideEffects) {
        is TvSeriesDetailsSideEffects.ShowLoadingState -> {
            movieDetailsState.value =
                MovieDetailsState.LOADING
        }
        is TvSeriesDetailsSideEffects.ShowErrorState -> {
            movieDetailsState.value =
                MovieDetailsState.ERROR
        }
        is TvSeriesDetailsSideEffects.ShowMovieData -> {
            movieData.value = sideEffects.tvDetails
            movieDetailsState.value = MovieDetailsState.DEFAULT
            scope.launch {
                delay(300)
                isRatingVisible.value = true
            }
        }
        is TvSeriesDetailsSideEffects.OpenPlaylistSelector -> {
            scope.launch {
                playlistSelectViewModel.updateAllPlaylists()
                bottomSheetState.animateTo(ModalBottomSheetValue.Expanded)
            }
        }
    }
}
