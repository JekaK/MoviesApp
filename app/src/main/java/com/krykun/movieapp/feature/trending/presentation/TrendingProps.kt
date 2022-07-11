package com.krykun.movieapp.feature.trending.presentation

data class TrendingProps(
    val currentTrendingPageIndex: Int = -1,
    val scrollOffsetTrending: Float = 0f,
    val lastSavedPageTrending: Int = 0,
    val isShowLoading: Boolean = true
)
