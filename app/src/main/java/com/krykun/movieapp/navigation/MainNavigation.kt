package com.krykun.movieapp.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.krykun.movieapp.feature.discover.presentation.DiscoverMoviesViewModel
import com.krykun.movieapp.feature.home.presentation.HomeMoviesViewModel
import com.krykun.movieapp.feature.home.view.HomeView
import com.krykun.movieapp.feature.moviedetails.MovieDetailsView
import com.krykun.movieapp.feature.person.view.PersonView
import com.krykun.movieapp.feature.playlist.details.view.PlaylistDetailsView
import com.krykun.movieapp.feature.playlist.main.view.PlaylistView
import com.krykun.movieapp.feature.search.presentation.SearchViewModel
import com.krykun.movieapp.feature.search.view.SearchView
import com.krykun.movieapp.feature.splashscreen.presentation.SplashScreenViewModel
import com.krykun.movieapp.feature.splashscreen.view.AnimatedSplashScreen
import com.krykun.movieapp.feature.trending.presentation.TrendingViewModel
import com.krykun.movieapp.feature.tvseries.TvSeriesDetailsView

@OptIn(ExperimentalAnimationApi::class, ExperimentalMotionApi::class)
@Composable
fun MainNavigation(
    navController: NavHostController,
    innerPadding: PaddingValues
) {
    val homeViewModel: HomeMoviesViewModel = hiltViewModel()
    val discoverMoviesViewModel: DiscoverMoviesViewModel = hiltViewModel()
    val splashScreenViewModel: SplashScreenViewModel = hiltViewModel()
    val trendingViewModel: TrendingViewModel = hiltViewModel()
    val searchViewModel: SearchViewModel = hiltViewModel()

    AnimatedNavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(
            route = Screen.Splash.route,
        ) {
            AnimatedSplashScreen(
                navHostController = navController,
                viewModel = splashScreenViewModel
            )
        }
        composable(route = Screen.Home().route) {
            HomeView(
                viewModel = homeViewModel,
                discoverMoviesViewModel = discoverMoviesViewModel,
                trendingViewModel = trendingViewModel,
                navHostController = navController
            )
        }
        composable(route = Screen.Search().route) {
            SearchView(
                viewModel = searchViewModel,
                navHostController = navController,
                innerPadding = innerPadding
            )
        }
        composable(route = Screen.Favourite().route) {
            PlaylistView(navHostController = navController)
        }
        composable(route = Screen.MovieDetails().route) {
            MovieDetailsView(navHostController = navController)
        }
        composable(route = Screen.TvSeriesDetails().route) {
            TvSeriesDetailsView(navHostController = navController)
        }
        composable(route = Screen.PersonDetails().route) {
            PersonView(navHostController = navController)
        }
        composable(route = Screen.PlaylistDetails().route) {
            PlaylistDetailsView(navHostController = navController)
        }
    }
}