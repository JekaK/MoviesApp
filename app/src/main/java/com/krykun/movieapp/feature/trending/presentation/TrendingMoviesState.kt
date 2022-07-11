package com.krykun.movieapp.feature.trending.presentation

data class TrendingMoviesState(
    val currentPageIndex: Int = -1,
    val scrollOffset: Float = 0f,
    val lastSavedPage: Int = 0,
    val trendingMovieType: SelectedMovieType = SelectedMovieType.TRENDING(),
    val popularMovieType: SelectedMovieType = SelectedMovieType.POPULAR(),
    val topRatedMovieType: SelectedMovieType = SelectedMovieType.TOPRATED(),
    val selectedMovieType: SelectedMovieType = SelectedMovieType.TRENDING(),
)

sealed class SelectedMovieType(
    open val title: String,
    open val loadingState: LoadingState
) {
    data class TRENDING(
        override val title: String = "Trending",
        override val loadingState: LoadingState = LoadingState.LOADING
    ) : SelectedMovieType(
        title,
        loadingState
    )

    data class POPULAR(
        override val title: String = "Popular",
        override val loadingState: LoadingState = LoadingState.LOADING
    ) : SelectedMovieType(
        title,
        loadingState
    )

    data class TOPRATED(
        override val title: String = "Top Rated",
        override val loadingState: LoadingState = LoadingState.LOADING
    ) : SelectedMovieType(
        title,
        loadingState
    )
}

enum class LoadingState {
    LOADING,
    STATIONARY
}