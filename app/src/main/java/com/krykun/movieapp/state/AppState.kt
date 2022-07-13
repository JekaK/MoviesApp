package com.krykun.movieapp.state

import com.krykun.movieapp.feature.home.presentation.HomeState
import com.krykun.movieapp.feature.moviedetails.presentation.MovieDetailsState
import com.krykun.movieapp.feature.search.presentation.SearchState
import com.krykun.movieapp.feature.splashscreen.presentation.SplashScreenState
import com.krykun.movieapp.feature.tvseries.presentation.TvSeriesDetailsState
import com.krykun.movieapp.feature.tvseries.presentation.TvSeriesState

data class AppState(
    val homeState: HomeState = HomeState(),
    val splashScreenState: SplashScreenState = SplashScreenState(),
    val baseMoviesState: BaseMoviesState = BaseMoviesState(),
    val movieDetailsState: MovieDetailsState = MovieDetailsState(),
    val searchState: SearchState = SearchState(),
    val tvSeriesState: TvSeriesDetailsState = TvSeriesDetailsState(),
)