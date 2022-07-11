package com.krykun.movieapp.feature.trending.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.LocalOverScrollConfiguration
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import com.krykun.domain.model.movies.Movie
import com.krykun.movieapp.custom.snaper.SnapOffsets
import com.krykun.movieapp.custom.snaper.rememberSnapperFlingBehavior
import com.krykun.movieapp.ext.collectAndHandleState
import com.krykun.movieapp.feature.trending.presentation.LoadingState
import com.krykun.movieapp.feature.trending.presentation.SelectedMovieType
import com.krykun.movieapp.feature.trending.presentation.TrendingMoviesSideEffects
import com.krykun.movieapp.feature.trending.presentation.TrendingViewModel
import com.krykun.movieapp.navigation.Screen
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(
    com.krykun.movieapp.custom.snaper.ExperimentalSnapperApi::class
)
@Composable
fun TrendingView(
    viewModel: TrendingViewModel,
    navHostController: NavHostController,
) {
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current

    val trendingMovies: LazyPagingItems<Movie> =
        viewModel.getTrendingMovies.collectAndHandleState(viewModel::handleLoadState)
    val popularMovies: LazyPagingItems<Movie>? =
        viewModel.getPopularMovies?.collectAndHandleState(viewModel::handleLoadState)

    val trendingLazyListState = rememberLazyListState()
    val popularLazyListState = rememberLazyListState()

    val scope = rememberCoroutineScope()

    val selectedMovieType = remember {
        mutableStateOf<SelectedMovieType>(SelectedMovieType.TRENDING())
    }
    val showLoading = remember {
        mutableStateOf(true)
    }

    val movieTypeList = listOf(
        SelectedMovieType.TRENDING(),
        SelectedMovieType.POPULAR()
    )

    Column(
        modifier = Modifier
            .height(250.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        SelectableItemsView(
            items = movieTypeList,
            selectedIndex = movieTypeList.indexOf(movieTypeList.find {
                selectedMovieType.value.title == it.title
            }),
            viewModel = viewModel
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (!showLoading.value) {
            when (selectedMovieType.value) {
                is SelectedMovieType.TRENDING -> {
                    BasicMoviesItem(
                        lazyPaging = trendingMovies,
                        lazyListState = trendingLazyListState,
                        viewModel = viewModel,
                        navHostController = navHostController,
                    )
                }
                is SelectedMovieType.POPULAR -> {
                    popularMovies?.let {
                        BasicMoviesItem(
                            lazyPaging = it,
                            lazyListState = popularLazyListState,
                            viewModel = viewModel,
                            navHostController = navHostController,
                        )
                    }
                }
            }
        } else {
            LoadingView()
        }
    }

    //TODO remove this when HorizontalPager will remember scroll position when recomposing
    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) {
                when (selectedMovieType.value) {
                    SelectedMovieType.TRENDING() -> {
                        viewModel.setLastScrolledPage(trendingLazyListState.firstVisibleItemIndex)
                        viewModel.setScrollOffset(trendingLazyListState.firstVisibleItemScrollOffset.toFloat())
                    }
                    SelectedMovieType.POPULAR() -> {
                        viewModel.setLastScrolledPage(popularLazyListState.firstVisibleItemIndex)
                        viewModel.setScrollOffset(popularLazyListState.firstVisibleItemScrollOffset.toFloat())
                    }
                }
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    //TODO remove this when HorizontalPager will remember scroll position when recomposing
    LaunchedEffect(key1 = trendingMovies.loadState.refresh) {
        if (trendingMovies.loadState.refresh is LoadState.NotLoading) {
            viewModel.getCurrentPageAndScrollOffset()
            viewModel.setSelectedMovieType(
                SelectedMovieType.TRENDING(loadingState = LoadingState.STATIONARY),
                isSelecting = false
            )
        } else if (trendingMovies.loadState.refresh is LoadState.Loading) {
            viewModel.setSelectedMovieType(
                SelectedMovieType.TRENDING(loadingState = LoadingState.LOADING),
                isSelecting = false
            )
        }
    }

    //TODO remove this when HorizontalPager will remember scroll position when recomposing
    LaunchedEffect(key1 = popularMovies?.loadState?.refresh) {
        if (popularMovies?.loadState?.refresh is LoadState.NotLoading) {
            viewModel.getCurrentPageAndScrollOffset()
            viewModel.setSelectedMovieType(
                SelectedMovieType.POPULAR(loadingState = LoadingState.STATIONARY),
                isSelecting = false
            )
        } else if (trendingMovies.loadState.refresh is LoadState.Loading) {
            viewModel.setSelectedMovieType(
                SelectedMovieType.POPULAR(loadingState = LoadingState.LOADING),
                isSelecting = false
            )
        }
    }

    LaunchedEffect(key1 = Unit) {
        scope.launch {
            viewModel.subscribeToStateUpdate()
                .collect {
                    showLoading.value = it.isShowLoading
                }
        }
    }

    viewModel.collectSideEffect {
        handleSideEffects(
            it,
            trendingMovies,
            trendingLazyListState,
            popularLazyListState,
            selectedMovieType,
            viewModel,
            scope,
        )
    }
}

@OptIn(
    ExperimentalFoundationApi::class, ExperimentalSnapperApi::class,
    com.krykun.movieapp.custom.snaper.ExperimentalSnapperApi::class
)
@Composable
private fun BasicMoviesItem(
    lazyPaging: LazyPagingItems<Movie>,
    lazyListState: LazyListState,
    viewModel: TrendingViewModel,
    navHostController: NavHostController
) {

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
            items(items = lazyPaging) { movie ->
                Card(
                    shape = RoundedCornerShape(20.dp)
                ) {
                    movie?.let {
                        TrendingItemView(
                            moviesItem = it,
                            modifier = Modifier
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

private fun handleSideEffects(
    sideEffects: TrendingMoviesSideEffects,
    movies: LazyPagingItems<Movie>,
    trendingLazyListState: LazyListState,
    popularLazyListState: LazyListState,
    selectedMovieType: MutableState<SelectedMovieType>,
    viewModel: TrendingViewModel,
    scope: CoroutineScope
) {
    when (sideEffects) {
        is TrendingMoviesSideEffects.GetCurrentTrendingPageAndScrollOffset -> {
            val currentPage = sideEffects.currentPageAndOffset
            scope.launch {
                when (selectedMovieType.value) {
                    SelectedMovieType.TRENDING() -> {
                        trendingLazyListState.scrollToItem(currentPage)
                    }
                    SelectedMovieType.POPULAR() -> {
                        popularLazyListState.scrollToItem(currentPage)
                    }
                }
            }
        }
        is TrendingMoviesSideEffects.TryReloadTrendingPage -> {
            movies.retry()
        }
        is TrendingMoviesSideEffects.ChangeMoviesSelectedItem -> {
            if (selectedMovieType.value.loadingState != LoadingState.LOADING) {
                viewModel.setLastScrolledPage(0)
                viewModel.setScrollOffset(0f)
            }
            selectedMovieType.value = sideEffects.selectedMovieType
        }
    }
}