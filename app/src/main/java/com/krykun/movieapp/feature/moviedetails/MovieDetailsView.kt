package com.krykun.movieapp.feature.moviedetails

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import androidx.hilt.navigation.compose.hiltViewModel
import com.krykun.data.util.Constants
import com.krykun.domain.model.moviedetails.MovieDetails
import com.krykun.movieapp.R
import com.krykun.movieapp.feature.moviedetails.presentation.MovieDetailsSideEffects
import com.krykun.movieapp.feature.moviedetails.presentation.MovieDetailsViewModel
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.coil.CoilImage
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalMaterialApi::class)
@ExperimentalMotionApi
@Composable
fun MovieDetailsView(viewModel: MovieDetailsViewModel = hiltViewModel()) {
    val context = LocalContext.current

    val heightInPx = with(LocalDensity.current) { 150.dp.toPx() }
    val swipingState = rememberSwipeableState(initialValue = SwipingStates.EXPANDED)
    val movieData = remember {
        mutableStateOf<MovieDetails?>(null)
    }
    val motionScene = remember {
        context.resources.openRawResource(R.raw.details_motion_scene)
            .readBytes()
            .decodeToString()
    }

    MotionLayout(
        motionScene = MotionScene(content = motionScene),
        progress = if (swipingState.progress.to == SwipingStates.COLLAPSED) swipingState.progress.fraction
        else 1f - swipingState.progress.fraction,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
    ) {
        val contentAttr = motionProperties(id = "content")
        val titleAttr = motionProperties(id = "title")
        val imageAttr = motionProperties(id = "film_image")
        Box(
            modifier = Modifier
                .layoutId("content")
                .clip(
                    RoundedCornerShape(
                        topStart = contentAttr.value
                            .int("corner")
                            .toFloat()
                    )
                )
                .background(Color.LightGray)
                .swipeable(
                    state = swipingState,
                    thresholds = { _, _ -> FractionalThreshold(0.5f) },
                    orientation = Orientation.Vertical,
                    anchors = mapOf(
                        0f to SwipingStates.COLLAPSED,
                        heightInPx to SwipingStates.EXPANDED,
                    )
                )
        )

        Box(
            modifier = Modifier
                .layoutId("film_image")
                .clip(
                    RoundedCornerShape(
                        imageAttr.value
                            .int("corner")
                            .toFloat()
                    )
                )
        ) {
            CoilImage(
                imageModel = Constants.IMAGE_BASE_URL + movieData.value?.posterPath,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                circularReveal = CircularReveal(duration = 350),
                placeHolder = ImageVector.vectorResource(id = R.drawable.ic_movie_placeholder),
                error = ImageVector.vectorResource(id = R.drawable.ic_movie_placeholder),
            )

        }
        Text(
            modifier = Modifier.layoutId("title"),
            text = movieData.value?.originalTitle ?: "",
            fontSize = titleAttr.value.fontSize("fontSize")
        )
    }
    viewModel.collectSideEffect {
        handleSideEffects(
            it,
            movieData
        )
    }
}

fun handleSideEffects(
    sideEffects: MovieDetailsSideEffects,
    movieData: MutableState<MovieDetails?>
) {
    when (sideEffects) {
        is MovieDetailsSideEffects.ShowLoadingState -> {

        }
        is MovieDetailsSideEffects.ShowErrorState -> {

        }
        is MovieDetailsSideEffects.ShowMovieData -> {
            movieData.value = sideEffects.movieDetails
        }
    }
}

enum class SwipingStates {
    EXPANDED,
    COLLAPSED
}