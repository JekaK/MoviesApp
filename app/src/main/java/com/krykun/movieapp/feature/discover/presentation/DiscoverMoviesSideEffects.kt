package com.krykun.movieapp.feature.discover.presentation

sealed class DiscoverMoviesSideEffects {
    data class TriggerOnPageChanged(val index: Int) : DiscoverMoviesSideEffects()
    data class GetCurrentPageAndScrollOffset(
        val currentPageAndOffset: Int
    ) : DiscoverMoviesSideEffects()

    object TryReloadUpcomingPage : DiscoverMoviesSideEffects()
}