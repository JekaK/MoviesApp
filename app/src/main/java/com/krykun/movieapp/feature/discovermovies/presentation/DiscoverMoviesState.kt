package com.krykun.movieapp.feature.discovermovies.presentation

import com.krykun.domain.model.MovieDiscoverItem

data class DiscoverMoviesState(
    val isOpen: Boolean = false,
    val moviesItems: List<MovieDiscoverItem> = listOf()
)