package com.krykun.movieapp.feature.trending.presentation

data class TrendingMoviesState(
    val currentTrendingPageIndex: Int = -1,
    val scrollOffsetTrending: Float = 0f,
    val lastSavedPageTrending: Int = 0,
    val trendingMovieType: SelectedMovieType = SelectedMovieType.TRENDING(),
    val popularMovieType: SelectedMovieType = SelectedMovieType.POPULAR(),
    val selectedMovieType: SelectedMovieType = SelectedMovieType.TRENDING()
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
}

enum class LoadingState {
    LOADING,
    STATIONARY
}