package com.krykun.movieapp.feature.discover.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.LocalOverScrollConfiguration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import com.krykun.data.util.Constants
import com.krykun.domain.model.MovieDiscoverItem
import com.krykun.movieapp.R
import com.krykun.movieapp.custom.DominantColorState
import com.krykun.movieapp.custom.DynamicThemePrimaryColorsFromImage
import com.krykun.movieapp.custom.rememberDominantColorState
import com.krykun.movieapp.custom.verticalGradientScrim
import com.krykun.movieapp.ext.contrastAgainst
import com.krykun.movieapp.ext.lerp
import com.krykun.movieapp.feature.discover.presentation.DiscoverMoviesSideEffects
import com.krykun.movieapp.feature.discover.presentation.viewmodel.UpcomingMoviesViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectSideEffect
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class, ExperimentalPagerApi::class)
@Composable
fun UpcomingView(viewModel: UpcomingMoviesViewModel) {
    val movies = viewModel.getDiscoverMovies.collectAsLazyPagingItems()

    val state = rememberUpdatedState(newValue = movies.loadState.refresh)
    val lazyListState = rememberPagerState()
    val scope = rememberCoroutineScope()

    val surfaceColor = MaterialTheme.colors.surface
    val dominantColorState = rememberDominantColorState { color ->
        color.contrastAgainst(surfaceColor) >= 3f
    }

    DynamicThemePrimaryColorsFromImage(dominantColorState) {
        Column(
            modifier = Modifier
                .height(350.dp)
                .fillMaxWidth()
                .verticalGradientScrim(
                    color = MaterialTheme.colors.primary,
                    startYPercentage = 1f,
                    endYPercentage = 0f
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.upcomming),
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))
            CompositionLocalProvider(
                LocalOverScrollConfiguration provides null
            ) {
                HorizontalPager(
                    count = movies.itemCount,
                    state = lazyListState,
                    modifier = Modifier
                        .fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 120.dp),
                ) { page ->
                    viewModel.triggerOnPageChanged(lazyListState.currentPage)
                    Card(
                        Modifier
                            .graphicsLayer {
                                val absolutePageOffset =
                                    calculateCurrentOffsetForPage(page).absoluteValue

                                val pageOffset = calculateCurrentOffsetForPage(page)

                                val degrees = if (pageOffset > 0) {
                                    360
                                } else {
                                    -360
                                }

                                rotationZ = degrees * lerp(
                                    start = 0.98f,
                                    stop = 1f,
                                    fraction = 1f - absolutePageOffset.coerceIn(0f, 1f)
                                )

                                lerp(
                                    start = 0.8f,
                                    stop = 1f,
                                    fraction = 1f - absolutePageOffset.coerceIn(0f, 1f)
                                ).also { scale ->
                                    scaleX = scale
                                    scaleY = scale
                                }

                                lerp(
                                    start = 0f,
                                    stop = 1f,
                                    fraction = 1f - absolutePageOffset.coerceIn(0f, 1f)
                                ).also { scale ->
                                    translationY = scale
                                }

                            },
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        movies[page]?.let { UpcomingItemView(moviesItem = it) }
                    }
                }
            }
        }
    }

    //TODO remove this when HorizontalPager will remember scroll position when recomposing
    DisposableEffect(key1 = true) {
        onDispose {
            viewModel.currentPage.value = lazyListState.currentPage
            viewModel.scrollOffset.value = lazyListState.currentPageOffset
        }
    }
    //TODO remove this when HorizontalPager will remember scroll position when recomposing
    LaunchedEffect(key1 = state.value) {
        if (state.value is LoadState.NotLoading) {
            lazyListState.scrollToPage(
                viewModel.currentPage.value,
                viewModel.scrollOffset.value
            )
        }
    }

    viewModel.collectSideEffect {
        handleSideEffects(
            it,
            movies,
            dominantColorState,
            scope
        )
    }
}


private fun handleSideEffects(
    sideEffects: DiscoverMoviesSideEffects,
    movies: LazyPagingItems<MovieDiscoverItem>,
    dominantColorState: DominantColorState,
    scope: CoroutineScope
) {
    when (sideEffects) {
        is DiscoverMoviesSideEffects.TriggerOnPageChanged -> {
            scope.launch {
                dominantColorState.updateColorsFromImageUrl(
                    Constants.IMAGE_BASE_URL +
                            movies[sideEffects.index]?.backdropPath
                )
            }
        }
    }
}