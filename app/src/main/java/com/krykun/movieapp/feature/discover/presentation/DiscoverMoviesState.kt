package com.krykun.movieapp.feature.discover.presentation

data class DiscoverMoviesState(
    val currentPageIndex: Int = -1,
    val scrollOffset: Int = 0,
    val lastSavedPage: Int = 0,
    val loadingState: LoadingState = LoadingState.LOADING
)

enum class LoadingState {
    LOADING,
    STATIONARY
}