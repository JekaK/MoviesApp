package com.krykun.movieapp.feature.discover.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.paging.compose.itemsIndexed
import com.krykun.data.util.Constants
import com.krykun.domain.model.remote.MovieDiscoverItem
import com.krykun.movieapp.R
import com.krykun.movieapp.custom.DominantColorState
import com.krykun.movieapp.custom.DynamicThemePrimaryColorsFromImage
import com.krykun.movieapp.custom.rememberDominantColorState
import com.krykun.movieapp.custom.snaper.ExperimentalSnapperApi
import com.krykun.movieapp.custom.snaper.SnapOffsets
import com.krykun.movieapp.custom.snaper.rememberSnapperFlingBehavior
import com.krykun.movieapp.custom.verticalGradientScrim
import com.krykun.movieapp.ext.calculateCurrentOffsetForPage
import com.krykun.movieapp.ext.collectAndHandleState
import com.krykun.movieapp.ext.contrastAgainst
import com.krykun.movieapp.ext.lerp
import com.krykun.movieapp.feature.discover.presentation.DiscoverMoviesSideEffects
import com.krykun.movieapp.feature.discover.presentation.DiscoverMoviesViewModel
import com.krykun.movieapp.navigation.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectSideEffect
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class, ExperimentalSnapperApi::class)
@Composable
fun DiscoverView(
    viewModel: DiscoverMoviesViewModel,
    navHostController: NavHostController,
) {
    val movies = viewModel.getDiscoverMovies.collectAndHandleState(viewModel::handleLoadState)
    val lazyListState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val surfaceColor = MaterialTheme.colors.surface
    val dominantColorState = rememberDominantColorState { color ->
        color.contrastAgainst(surfaceColor) >= 3f
    }
    val parentOffsetState = remember {
        mutableStateOf(Offset(0f, 0f))
    }

    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current

    viewModel.collectSideEffect {
        handleSideEffects(
            sideEffects = it,
            movies = movies,
            dominantColorState = dominantColorState,
            lazyListState = lazyListState,
            scope = scope,
            navHostController = navHostController
        )
    }

    if (movies.itemCount == 0) {
        LoadingView()
    } else {
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
                    LocalOverscrollConfiguration provides null
                ) {
                    LazyRow(
                        flingBehavior = rememberSnapperFlingBehavior(
                            lazyListState = lazyListState,
                            snapOffsetForItem = SnapOffsets.Center,
                            endContentPadding = 100.dp,
                            snapIndex = { layoutInfo, startIndex, targetIndex ->
                                viewModel.triggerOnPageChanged(targetIndex)
                                targetIndex.coerceIn(startIndex - 1, startIndex + 1)
                            }
                        ),
                        state = lazyListState,
                        modifier = Modifier
                            .fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 110.dp),
                    ) {
                        itemsIndexed(movies) { index, item ->
                            Card(
                                Modifier
                                    .graphicsLayer {
                                        val absolutePageOffset =
                                            lazyListState.calculateCurrentOffsetForPage(index).absoluteValue

                                        val pageOffset =
                                            lazyListState.calculateCurrentOffsetForPage(index)

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
                                movies[index]?.let {
                                    DiscoverItemView(
                                        moviesItem = it,
                                        modifier = Modifier
                                            .onGloballyPositioned {
                                                val offset = it.positionInRoot()
                                                parentOffsetState.value = offset
                                            }
                                            .pointerInput(Unit) {
                                                detectTapGestures(onTap = {
                                                    viewModel.navigateToMovieDetails(
                                                        movies.itemSnapshotList.items[index].id
                                                            ?: -1
                                                    )
                                                })
                                            }
                                    )
                                }
                            }
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
                val calculatedOffset =
                    lazyListState.calculateCurrentOffsetForPage(lazyListState.firstVisibleItemIndex)
                viewModel.setLastScrolledPage(
                    if (calculatedOffset != 0f) {
                        lazyListState.firstVisibleItemIndex + 1
                    } else {
                        lazyListState.firstVisibleItemIndex
                    }
                )
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
    LaunchedEffect(key1 = movies.loadState.refresh) {
        if (movies.loadState.refresh is LoadState.NotLoading) {
            viewModel.getCurrentPageAndScrollOffset()
        }
    }
}

private fun handleSideEffects(
    sideEffects: DiscoverMoviesSideEffects,
    movies: LazyPagingItems<MovieDiscoverItem>,
    dominantColorState: DominantColorState,
    lazyListState: LazyListState,
    scope: CoroutineScope,
    navHostController: NavHostController
) {
    when (sideEffects) {
        is DiscoverMoviesSideEffects.TriggerOnPageChanged -> {
            scope.launch {
                if (movies.itemCount >= sideEffects.index && movies.itemCount != 0) {
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
                lazyListState.scrollToItem(currentPage, 0)
            }
        }
        is DiscoverMoviesSideEffects.TryReloadDiscoverPage -> {
            movies.retry()
        }
        is DiscoverMoviesSideEffects.NavigateToMovie -> {
            navHostController.navigate(Screen.MovieDetails().route)
        }
    }
}