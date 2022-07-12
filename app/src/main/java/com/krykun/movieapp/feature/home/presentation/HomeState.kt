package com.krykun.movieapp.feature.home.presentation

import com.krykun.movieapp.feature.trending.presentation.TrendingMoviesState
import com.krykun.movieapp.feature.discover.presentation.DiscoverMoviesState

data class HomeState(
    val isOpen: Boolean = false,
    val discoverMoviesState: DiscoverMoviesState = DiscoverMoviesState(),
    val trendingMoviesState: TrendingMoviesState = TrendingMoviesState()
)