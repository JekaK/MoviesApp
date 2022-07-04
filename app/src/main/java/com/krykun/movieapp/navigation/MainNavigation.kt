package com.krykun.movieapp.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.krykun.movieapp.feature.discover.presentation.viewmodel.DiscoverMoviesViewModel
import com.krykun.movieapp.feature.discover.presentation.viewmodel.UpcomingMoviesViewModel
import com.krykun.movieapp.feature.discover.view.DiscoverView
import com.krykun.movieapp.feature.moviedetails.MovieDetailsView
import com.krykun.movieapp.feature.splashscreen.presentation.SplashScreenViewModel
import com.krykun.movieapp.feature.splashscreen.view.AnimatedSplashScreen

@OptIn(ExperimentalAnimationApi::class, ExperimentalMotionApi::class)
@Composable
fun MainNavigation(
    navController: NavHostController = rememberAnimatedNavController()
) {
    val viewModel: DiscoverMoviesViewModel = hiltViewModel()
    val upcomingMoviesViewModel: UpcomingMoviesViewModel = hiltViewModel()
    val splashScreenViewModel: SplashScreenViewModel = hiltViewModel()

    val configuration = LocalConfiguration.current
    val screenWidth = with(LocalDensity.current) { configuration.screenWidthDp.dp.roundToPx() }

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
            route = Screen.MovieDetails().route + "/{x}/{y}",
            arguments = listOf(navArgument("x") {
                type = NavType.FloatType
            },
                navArgument("y") {
                    type = NavType.FloatType
                }),
            enterTransition = {
                val x = this.targetState.arguments?.getFloat("x")
                val y = this.targetState.arguments?.getFloat("y")

                scaleIn(
                    animationSpec = tween(300),
                    transformOrigin = TransformOrigin(
                        pivotFractionX = x ?: 0f,
                        pivotFractionY = y ?: 0f
                    )
                )
            },
            exitTransition = {
                val x = this.targetState.arguments?.getFloat("x")
                val y = this.targetState.arguments?.getFloat("y")

                scaleOut(
                    animationSpec = tween(300),
                    transformOrigin = TransformOrigin(
                        pivotFractionX = x ?: 0f,
                        pivotFractionY = y ?: 0f
                    )
                )
            },
            popEnterTransition = {
                val x = this.targetState.arguments?.getFloat("x")
                val y = this.targetState.arguments?.getFloat("y")

                scaleIn(
                    animationSpec = tween(300),
                    transformOrigin = TransformOrigin(
                        pivotFractionX = x ?: 0f,
                        pivotFractionY = y ?: 0f
                    )
                )
            },
            popExitTransition = {
                val x = this.targetState.arguments?.getFloat("x")
                val y = this.targetState.arguments?.getFloat("y")

                scaleOut(
                    animationSpec = tween(300),
                    transformOrigin = TransformOrigin(
                        pivotFractionX = x ?: 0f,
                        pivotFractionY = y ?: 0f
                    )
                )
            }
        ) { backStackEntry ->
            MovieDetailsView()
        }
    }
}

@Composable
fun EmptyView() {
    Column(modifier = Modifier.fillMaxSize()) {

    }
}