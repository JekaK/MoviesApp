package com.krykun.movieapp.state

import com.krykun.movieapp.feature.discovermovies.presentation.DiscoverMoviesState

data class AppState(val discoverMoviesState: DiscoverMoviesState = DiscoverMoviesState())