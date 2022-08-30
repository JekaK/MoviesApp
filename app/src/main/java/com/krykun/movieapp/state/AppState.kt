package com.krykun.movieapp.state

import com.krykun.movieapp.feature.home.presentation.HomeState
import com.krykun.movieapp.feature.moviedetails.presentation.MovieDetailsState
import com.krykun.movieapp.feature.person.presentation.PersonDetailsState
import com.krykun.movieapp.feature.playlist.main.presentation.PlaylistState
import com.krykun.movieapp.feature.addtoplaylist.presentation.PlaylistSelectState
import com.krykun.movieapp.feature.search.presentation.SearchState
import com.krykun.movieapp.feature.splashscreen.presentation.SplashScreenState
import com.krykun.movieapp.feature.tvseries.presentation.TvSeriesDetailsState

data class AppState(
    val homeState: HomeState = HomeState(),
    val splashScreenState: SplashScreenState = SplashScreenState(),
    val baseMoviesState: BaseMoviesState = BaseMoviesState(),
    val movieDetailsState: List<MovieDetailsState> = listOf(),
    val searchState: SearchState = SearchState(),
    val tvSeriesState: TvSeriesDetailsState = TvSeriesDetailsState(),
    val personState: PersonDetailsState = PersonDetailsState(),
    val playlistState: PlaylistState = PlaylistState(),
    val playlistSelectState: PlaylistSelectState = PlaylistSelectState()
)