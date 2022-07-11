package com.krykun.movieapp.feature.discover.presentation

data class DiscoverMoviesState(
    val currentUpcomingPageIndex: Int = -1,
    val scrollOffsetUpcoming: Float = 0f,
    val lastSavedPageUpcoming: Int = 0
)