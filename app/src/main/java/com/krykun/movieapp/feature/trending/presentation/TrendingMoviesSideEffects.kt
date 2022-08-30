package com.krykun.movieapp.feature.trending.presentation

import com.krykun.movieapp.feature.discover.presentation.DiscoverMoviesSideEffects

sealed class TrendingMoviesSideEffects {
    data class GetCurrentTrendingPageAndScrollOffset(
        val currentPageAndOffset: Int
    ) : TrendingMoviesSideEffects()

    object TryReloadTrendingPage : TrendingMoviesSideEffects()

    object TryReloadPopularPage : TrendingMoviesSideEffects()

    object TryReloadTopRatedPage : TrendingMoviesSideEffects()

    object ChangeMoviesSelectedItem : TrendingMoviesSideEffects()

    object NavigateToMovie : TrendingMoviesSideEffects()

}