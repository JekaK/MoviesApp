package com.krykun.movieapp.feature.upcoming.presentation

sealed class UpcomingMoviesSideEffects {
    data class TriggerOnPageChanged(val index: Int) : UpcomingMoviesSideEffects()
    data class GetCurrentUpcomingPageAndScrollOffset(
        val currentPageAndOffset: Int
    ) : UpcomingMoviesSideEffects()

    object TryReloadUpcomingPage : UpcomingMoviesSideEffects()
}