package com.krykun.movieapp.feature.discover.presentation

sealed class DiscoverMoviesSideEffects {
    object ScreenOpen : DiscoverMoviesSideEffects()
    data class TriggerOnPageChanged(val index: Int) : DiscoverMoviesSideEffects()
    data class GetCurrentPageAndScrollOffset(
        val currentPageAndOffset: Pair<Int, Float>
    ) : DiscoverMoviesSideEffects()
}