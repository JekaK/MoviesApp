package com.krykun.movieapp.state

import com.krykun.movieapp.feature.discover.presentation.DiscoverMoviesState
import com.krykun.movieapp.feature.splashscreen.presentation.SplashScreenState

data class AppState(
    val discoverMoviesState: DiscoverMoviesState = DiscoverMoviesState(),
    val splashScreenState: SplashScreenState = SplashScreenState(),
    val baseMoviesState: BaseMoviesState = BaseMoviesState()
)