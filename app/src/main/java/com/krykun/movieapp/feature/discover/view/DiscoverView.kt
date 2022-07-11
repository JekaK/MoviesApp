package com.krykun.movieapp.feature.discover.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.LocalOverScrollConfiguration
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.google.accompanist.pager.*
import com.krykun.data.util.Constants
import com.krykun.domain.model.MovieDiscoverItem
import com.krykun.movieapp.R
import com.krykun.movieapp.custom.DominantColorState
import com.krykun.movieapp.custom.DynamicThemePrimaryColorsFromImage
import com.krykun.movieapp.custom.rememberDominantColorState
import com.krykun.movieapp.custom.verticalGradientScrim
import com.krykun.movieapp.ext.collectAndHandleState
import com.krykun.movieapp.ext.contrastAgainst
import com.krykun.movieapp.ext.lerp
import com.krykun.movieapp.feature.discover.presentation.DiscoverMoviesSideEffects
import com.krykun.movieapp.feature.discover.presentation.DiscoverMoviesViewModel
import com.krykun.movieapp.feature.home.view.upcoming.DiscoverItemView
import com.krykun.movieapp.navigation.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectSideEffect
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class, ExperimentalPagerApi::class)
@Composable
fun DiscoverView(
    viewModel: DiscoverMoviesViewModel,
    navHostController: NavHostController,
) {
    val movies = viewModel.getDiscoverMovies.collectAndHandleState(viewModel::handleLoadState)
    val state = rememberUpdatedState(newValue = movies.loadState.refresh)
    val lazyListState = rememberPagerState()
    val scope = rememberCoroutineScope()

    val surfaceColor = MaterialTheme.colors.surface
    val dominantColorState = rememberDominantColorState { color ->
        color.contrastAgainst(surfaceColor) >= 3f
    }
    val parentOffsetState = remember {
        mutableStateOf(Offset(0f, 0f))
    }

    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current

    DynamicThemePrimaryColorsFromImage(dominantColorState) {
        Column(
            modifier = Modifier
                .height(350.dp)
                .fillMaxWidth()
                .verticalGradientScrim(
                    color = MaterialTheme.colors.primary,
                    startYPercentage = 1f,
                    endYPercentage = 0.5f
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.discover),
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
                    contentPadding = PaddingValues(horizontal = 110.dp),
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
                        movies[page]?.let {
                            DiscoverItemView(
                                moviesItem = it,
                                modifier = Modifier
                                    .onGloballyPositioned {
                                        val offset = it.positionInRoot()
                                        parentOffsetState.value = offset
                                    }
                                    .pointerInput(Unit) {
                                        detectTapGestures(onTap = {
                                            if (page == lazyListState.currentPage ||
                                                page == lazyListState.currentPageOffset.absoluteValue.toInt()
                                            ) {
                                                viewModel.setMovieDetailsId(
                                                    movies.itemSnapshotList.items[lazyListState.currentPage].id
                                                        ?: -1
                                                )
                                                navHostController.navigate(Screen.MovieDetails().route)
                                            }
                                        })
                                    }
                            )
                        }
                    }
                }
            }
        }
    }

    //TODO remove this when HorizontalPager will remember scroll position when recomposing
    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) {
                viewModel.setLastScrolledPage(lazyListState.currentPage)
                viewModel.setScrollOffset(lazyListState.currentPageOffset)
            }
        }

        // Add the observer to the lifecycle
        lifecycleOwner.lifecycle.addObserver(observer)

        // When the effect leaves the Composition, remove the observer
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    //TODO remove this when HorizontalPager will remember scroll position when recomposing
    LaunchedEffect(key1 = state.value) {
        if (state.value is LoadState.NotLoading) {
            viewModel.getCurrentPageAndScrollOffset()
        }
    }
    viewModel.collectSideEffect {
        handleSideEffects(
            it,
            movies,
            dominantColorState,
            lazyListState,
            scope
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
private fun handleSideEffects(
    sideEffects: DiscoverMoviesSideEffects,
    movies: LazyPagingItems<MovieDiscoverItem>,
    dominantColorState: DominantColorState,
    lazyListState: PagerState,
    scope: CoroutineScope
) {
    when (sideEffects) {
        is DiscoverMoviesSideEffects.TriggerOnPageChanged -> {
            scope.launch {
                if (movies.itemCount >= sideEffects.index) {
                    dominantColorState.updateColorsFromImageUrl(
                        Constants.IMAGE_BASE_URL +
                                movies[sideEffects.index]?.backdropPath
                    )
                }
            }
        }
        is DiscoverMoviesSideEffects.GetCurrentDiscoverPageAndScrollOffset -> {
            val currentPage = sideEffects.currentPageAndOffset
            scope.launch {
                lazyListState.scrollToPage(currentPage, 0f)
            }
        }
        is DiscoverMoviesSideEffects.TryReloadDiscoverPage -> {
            movies.retry()
        }
    }
}