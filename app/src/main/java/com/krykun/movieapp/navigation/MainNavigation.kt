package com.krykun.movieapp.navigation


import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.krykun.domain.model.persondetails.PersonDetails
import com.krykun.movieapp.feature.home.presentation.HomeMoviesViewModel
import com.krykun.movieapp.feature.home.view.DiscoverView
import com.krykun.movieapp.feature.tvseries.TvSeriesDetailsView
import com.krykun.movieapp.feature.splashscreen.presentation.SplashScreenViewModel
import com.krykun.movieapp.feature.splashscreen.view.AnimatedSplashScreen
import com.krykun.movieapp.feature.trending.presentation.TrendingViewModel
import com.krykun.movieapp.feature.discover.presentation.DiscoverMoviesViewModel
import com.krykun.movieapp.feature.moviedetails.MovieDetailsView
import com.krykun.movieapp.feature.person.view.PersonView
import com.krykun.movieapp.feature.search.presentation.SearchViewModel
import com.krykun.movieapp.feature.search.view.SearchView


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
        composable(route = Screen.Discover().route) {
            DiscoverView(
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
            EmptyView()
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
    }
}

@Composable
fun EmptyView() {
    Column(modifier = Modifier.fillMaxSize()) {

    }
}