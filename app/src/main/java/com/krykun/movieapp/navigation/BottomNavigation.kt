package com.krykun.movieapp.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.krykun.movieapp.feature.discover.presentation.viewmodel.DiscoverMoviesViewModel
import com.krykun.movieapp.feature.discover.presentation.viewmodel.UpcomingMoviesViewModel
import com.krykun.movieapp.feature.discover.view.DiscoverView

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BottomNavigation(
    navController: NavHostController
) {
    val viewModel: DiscoverMoviesViewModel = hiltViewModel()
    val upcomingMoviesViewModel: UpcomingMoviesViewModel = hiltViewModel()

    val configuration = LocalConfiguration.current
    val screenWidth = with(LocalDensity.current) { configuration.screenWidthDp.dp.roundToPx() }
    AnimatedNavHost(
        navController = navController,
        startDestination = Screen.Discover().route
    ) {
        composable(route = Screen.Discover().route,
            enterTransition = {
                slideInHorizontally(initialOffsetX = { -screenWidth })
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -screenWidth })
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { -screenWidth })
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { screenWidth })
            }
        ) {
            DiscoverView(
                viewModel = viewModel,
                upcomingMoviesViewModel = upcomingMoviesViewModel
            )
        }
        composable(route = Screen.Search().route,
            enterTransition = {
                if (this.initialState.destination.hierarchy.any { it.route == Screen.Discover().route }) {
                    slideInHorizontally(initialOffsetX = { screenWidth })
                } else {
                    slideInHorizontally(initialOffsetX = { -screenWidth })
                }
            },
            exitTransition = {
                if (this.targetState.destination.hierarchy.any { it.route == Screen.Discover().route }) {
                    slideOutHorizontally(targetOffsetX = { screenWidth })
                } else {
                    slideOutHorizontally(targetOffsetX = { -screenWidth })
                }
            },
            popEnterTransition = {
                if (this.initialState.destination.hierarchy.any { it.route == Screen.Discover().route }) {
                    slideInHorizontally(initialOffsetX = { screenWidth })
                } else {
                    slideInHorizontally(initialOffsetX = { -screenWidth })
                }
            },
            popExitTransition = {
                if (this.targetState.destination.hierarchy.any { it.route == Screen.Discover().route }) {
                    slideOutHorizontally(targetOffsetX = { screenWidth })
                } else {
                    slideOutHorizontally(targetOffsetX = { -screenWidth })
                }
            }) {
            EmptyView()
        }
        composable(route = Screen.Favourite().route,
            enterTransition = {
                slideInHorizontally(initialOffsetX = { screenWidth })
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { screenWidth })
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { screenWidth })
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { screenWidth })
            }) {
            EmptyView()
        }
    }
}

@Composable
fun EmptyView() {
    Column(modifier = Modifier.fillMaxSize()) {

    }
}