package com.krykun.movieapp.feature.trending.view

import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.LocalOverScrollConfiguration
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import com.krykun.domain.model.trending.TrendingMovie
import com.krykun.movieapp.R
import com.krykun.movieapp.custom.snaper.SnapOffsets
import com.krykun.movieapp.custom.snaper.rememberSnapperFlingBehavior
import com.krykun.movieapp.ext.collectAndHandleState
import com.krykun.movieapp.ext.lerp
import com.krykun.movieapp.feature.trending.presentation.TrendingMoviesSideEffects
import com.krykun.movieapp.feature.trending.presentation.TrendingViewModel
import com.krykun.movieapp.navigation.Screen
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectSideEffect
import kotlin.math.absoluteValue

@OptIn(
    ExperimentalFoundationApi::class, ExperimentalSnapperApi::class,
    com.krykun.movieapp.custom.snaper.ExperimentalSnapperApi::class
)
@Composable
fun TrendingView(
    viewModel: TrendingViewModel,
    navHostController: NavHostController,
) {
    val movies = viewModel.getTrendingMovies.collectAndHandleState(viewModel::handleLoadState)
    val state = rememberUpdatedState(newValue = movies.loadState.refresh)
    val lazyListState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val parentOffsetState = remember {
        mutableStateOf(Offset(0f, 0f))
    }

    Column(
        modifier = Modifier
            .height(250.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.trending),
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(16.dp))
        CompositionLocalProvider(
            LocalOverScrollConfiguration provides null
        ) {
            LazyRow(
                state = lazyListState,
                modifier = Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                flingBehavior = rememberSnapperFlingBehavior(
                    lazyListState = lazyListState,
                    snapOffsetForItem = SnapOffsets.Start,
                    snapIndex = { layoutInfo, startIndex, targetIndex ->
                        targetIndex.coerceIn(startIndex - 1, startIndex + 1)
                    }
                ),
            ) {
                items(items = movies) { movie ->
                    Card(
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        movie?.let {
                            TrendingItemView(
                                moviesItem = it,
                                modifier = Modifier
                                    .onGloballyPositioned {
                                        val offset = it.positionInRoot()
                                        parentOffsetState.value = offset
                                    }
                                    .pointerInput(Unit) {
                                        detectTapGestures(onTap = {
                                            viewModel.setMovieDetailsId(movie.id ?: -1)
                                            navHostController.navigate(Screen.MovieDetails().route)
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
    DisposableEffect(key1 = Unit) {
        onDispose {
            viewModel.setLastScrolledPage(lazyListState.firstVisibleItemIndex)
            viewModel.setScrollOffset(lazyListState.firstVisibleItemScrollOffset.toFloat())
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
            lazyListState,
            scope
        )
    }
}

private fun handleSideEffects(
    sideEffects: TrendingMoviesSideEffects,
    movies: LazyPagingItems<TrendingMovie>,
    lazyListState: LazyListState,
    scope: CoroutineScope
) {
    when (sideEffects) {
        is TrendingMoviesSideEffects.GetCurrentTrendingPageAndScrollOffset -> {
            val currentPage = sideEffects.currentPageAndOffset
            scope.launch {
                lazyListState.scrollToItem(currentPage)
            }
        }
        is TrendingMoviesSideEffects.TryReloadTrendingPage -> {
            movies.retry()
        }
    }
}