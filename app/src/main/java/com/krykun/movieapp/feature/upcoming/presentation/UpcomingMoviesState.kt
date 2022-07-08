package com.krykun.movieapp.feature.upcoming.presentation

data class UpcomingMoviesState(
    val currentUpcomingPageIndex: Int = -1,
    val scrollOffsetUpcoming: Float = 0f,
    val lastSavedPageUpcoming: Int = 0
)