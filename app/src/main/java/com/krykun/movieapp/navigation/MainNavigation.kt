package com.krykun.movieapp.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.krykun.movieapp.feature.main.MainView
import com.krykun.movieapp.feature.splashscreen.presentation.SplashScreenViewModel
import com.krykun.movieapp.feature.splashscreen.view.AnimatedSplashScreen


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SetupNavGraph(
    navController: NavHostController = rememberAnimatedNavController()
) {
    val splashScreenViewModel: SplashScreenViewModel = hiltViewModel()

    AnimatedNavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(
            route = Screen.Splash.route,
        ) {
            AnimatedSplashScreen(
                navHostController = navController,
                viewModel = splashScreenViewModel
            )
        }
        composable(route = Screen.Main.route) {
            MainView()
        }
    }
}
