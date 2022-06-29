package com.krykun.movieapp.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.krykun.movieapp.feature.discovermovies.presentation.DiscoverMoviesViewModel
import com.krykun.movieapp.feature.discovermovies.view.DiscoverMoviesView

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BottomNavigation(
    navController: NavHostController
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = Screen.Discover().route
    ) {
        composable(route = Screen.Discover().route) {
            val viewModel: DiscoverMoviesViewModel = hiltViewModel()
            DiscoverMoviesView(viewModel = viewModel)
        }
        composable(route = Screen.Search().route) {
            EmptyView()
        }
        composable(route = Screen.Favourite().route) {
            EmptyView()
        }
    }
}

@Composable
fun EmptyView() {

}