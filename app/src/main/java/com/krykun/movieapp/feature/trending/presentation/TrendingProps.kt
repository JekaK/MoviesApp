package com.krykun.movieapp.feature.trending.presentation

data class TrendingProps(
    val currentTrendingPageIndex: Int,
    val scrollOffsetTrending: Float,
    val lastSavedPageTrending: Int,
    val isShowLoading: Boolean,
    val selectedMovieType: SelectedMovieType
)
