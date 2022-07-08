package com.krykun.movieapp.feature.trending.presentation

data class TrendingMoviesState(
    val currentTrendingPageIndex: Int = -1,
    val scrollOffsetTrending: Float = 0f,
    val lastSavedPageTrending: Int = 0
)
