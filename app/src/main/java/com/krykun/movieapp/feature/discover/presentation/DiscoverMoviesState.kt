package com.krykun.movieapp.feature.discover.presentation

data class DiscoverMoviesState(
    val currentPageIndex: Int = -1,
    val lastSavedPage: Int = -1,
    val loadingState: LoadingState = LoadingState.LOADING
)

enum class LoadingState {
    LOADING,
    STATIONARY
}