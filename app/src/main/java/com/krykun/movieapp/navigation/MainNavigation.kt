package com.krykun.movieapp.navigation


import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.krykun.movieapp.feature.discover.presentation.DiscoverMoviesViewModel
import com.krykun.movieapp.feature.discover.view.DiscoverView
import com.krykun.movieapp.feature.moviedetails.MovieDetailsView
import com.krykun.movieapp.feature.splashscreen.presentation.SplashScreenViewModel
import com.krykun.movieapp.feature.splashscreen.view.AnimatedSplashScreen
import com.krykun.movieapp.feature.trending.presentation.TrendingViewModel
import com.krykun.movieapp.feature.upcoming.presentation.UpcomingMoviesViewModel


@OptIn(ExperimentalAnimationApi::class, ExperimentalMotionApi::class)
@Composable
fun MainNavigation(
    navController: NavHostController
) {
    val viewModel: DiscoverMoviesViewModel = hiltViewModel()
    val upcomingMoviesViewModel: UpcomingMoviesViewModel = hiltViewModel()
    val splashScreenViewModel: SplashScreenViewModel = hiltViewModel()
    val trendingViewModel: TrendingViewModel = hiltViewModel()

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
                viewModel = viewModel,
                upcomingMoviesViewModel = upcomingMoviesViewModel,
                trendingViewModel = trendingViewModel,
                navHostController = navController
            )
        }
        composable(route = Screen.Search().route) {
            EmptyView()
        }
        composable(route = Screen.Favourite().route) {
            EmptyView()
        }
        composable(
            route = Screen.MovieDetails().route,
        ) {
            MovieDetailsView(navHostController = navController)
        }
    }
}

@Composable
fun EmptyView() {
    Column(modifier = Modifier.fillMaxSize()) {

    }
}