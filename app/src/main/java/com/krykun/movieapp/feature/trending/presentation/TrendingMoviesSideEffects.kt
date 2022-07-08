package com.krykun.movieapp.feature.trending.presentation

sealed class TrendingMoviesSideEffects {
    data class GetCurrentTrendingPageAndScrollOffset(
        val currentPageAndOffset: Int
    ) : TrendingMoviesSideEffects()

    object TryReloadTrendingPage : TrendingMoviesSideEffects()
}