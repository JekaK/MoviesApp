package com.krykun.movieapp.state

import com.krykun.movieapp.feature.discover.presentation.DiscoverMoviesState

data class AppState(val discoverMoviesState: DiscoverMoviesState = DiscoverMoviesState())