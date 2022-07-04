package com.krykun.movieapp.feature.discover.presentation

import com.krykun.domain.model.MovieDiscoverItem

data class DiscoverMoviesState(
    val isOpen: Boolean = false,
    val moviesItems: List<MovieDiscoverItem> = listOf(),
    val currentPageIndex: Int = -1,
    val scrollOffset: Float = 0f,
    val lastSavedPage: Int = 0
)