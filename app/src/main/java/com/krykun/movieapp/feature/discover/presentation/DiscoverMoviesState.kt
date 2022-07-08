package com.krykun.movieapp.feature.discover.presentation

import com.krykun.movieapp.feature.trending.presentation.TrendingMoviesState
import com.krykun.movieapp.feature.upcoming.presentation.UpcomingMoviesState

data class DiscoverMoviesState(
    val isOpen: Boolean = false,
    val upcomingMoviesState: UpcomingMoviesState = UpcomingMoviesState(),
    val trendingMoviesState: TrendingMoviesState = TrendingMoviesState()
)