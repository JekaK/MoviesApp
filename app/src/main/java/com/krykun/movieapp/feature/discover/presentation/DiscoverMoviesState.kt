package com.krykun.movieapp.feature.discover.presentation

data class DiscoverMoviesState(
    val currentUpcomingPageIndex: Int = -1,
    val scrollOffsetUpcoming: Int = 0,
    val lastSavedPageUpcoming: Int = 0,
    val loadingState: LoadingState = LoadingState.LOADING
)

enum class LoadingState {
    LOADING,
    STATIONARY
}