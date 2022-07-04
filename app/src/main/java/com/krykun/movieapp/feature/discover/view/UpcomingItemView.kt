package com.krykun.movieapp.feature.discover.view

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.krykun.data.util.Constants
import com.krykun.domain.model.MovieDiscoverItem
import com.krykun.movieapp.R
import com.krykun.movieapp.feature.discover.data.ScreenCoordinates
import com.krykun.movieapp.navigation.Screen
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.coil.CoilImage


@Composable
fun UpcomingItemView(
    moviesItem: MovieDiscoverItem,
    navHostController: NavHostController,
) {
    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp
    val screenWidth = configuration.screenWidthDp

    val screenWidthInPx = with(LocalDensity.current) { screenWidth.dp.roundToPx() }
    val screenHeightInPx = with(LocalDensity.current) { screenHeight.dp.roundToPx() }

    val parentOffsetState = remember {
        mutableStateOf(Offset(0f, 0f))
    }

    Box(
        modifier = Modifier
            .width(200.dp)
            .background(colorResource(id = R.color.container_background))
            .onGloballyPositioned {
                val offset = it.positionInRoot()
                parentOffsetState.value = offset
            }
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    navHostController.currentBackStackEntry
                        ?.arguments?.putParcelable(
                            "coordinates", ScreenCoordinates(
                                xFraction = it.x / (screenWidthInPx),
                                yFraction = parentOffsetState.value.y / (screenHeightInPx)
                            )
                        )
                    navHostController.navigate(
                        "${Screen.MovieDetails().route}/${
                            it.x / (screenWidthInPx)
                        }/${parentOffsetState.value.y / (screenHeightInPx)}"
                    )
                })
            }
    ) {
        CoilImage(
            imageModel = Constants.IMAGE_BASE_URL + moviesItem.posterPath,
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
                text = moviesItem.title ?: "",
                color = Color.White,
                modifier = Modifier.padding(
                    start = 12.dp,
                    end = 12.dp,
                    top = 12.dp,
                    bottom = 15.dp
                ),
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = moviesItem.mappedGenreIds.joinToString(separator = " | "),
                color = Color.White,
                modifier = Modifier.padding(
                    start = 12.dp,
                    end = 12.dp,
                    top = 6.dp,
                    bottom = 15.dp
                ),
                fontWeight = FontWeight.Normal,
                fontSize = 11.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

        }
    }
}