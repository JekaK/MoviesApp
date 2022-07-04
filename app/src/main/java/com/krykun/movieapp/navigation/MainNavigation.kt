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
import com.krykun.movieapp.feature.discover.presentation.viewmodel.DiscoverMoviesViewModel
import com.krykun.movieapp.feature.discover.presentation.viewmodel.UpcomingMoviesViewModel
import com.krykun.movieapp.feature.discover.view.DiscoverView
import com.krykun.movieapp.feature.moviedetails.MovieDetailsView
import com.krykun.movieapp.feature.splashscreen.presentation.SplashScreenViewModel
import com.krykun.movieapp.feature.splashscreen.view.AnimatedSplashScreen


@OptIn(ExperimentalAnimationApi::class, ExperimentalMotionApi::class)
@Composable
fun MainNavigation(
    navController: NavHostController
) {
    val viewModel: DiscoverMoviesViewModel = hiltViewModel()
    val upcomingMoviesViewModel: UpcomingMoviesViewModel = hiltViewModel()
    val splashScreenViewModel: SplashScreenViewModel = hiltViewModel()

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
            MovieDetailsView()
        }
    }
}

@Composable
fun EmptyView() {
    Column(modifier = Modifier.fillMaxSize()) {

    }
}