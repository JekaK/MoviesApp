package com.krykun.movieapp.feature.splashscreen.view

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.krykun.movieapp.feature.splashscreen.presentation.SplashScreenSideEffect
import com.krykun.movieapp.feature.splashscreen.presentation.SplashScreenViewModel
import com.capgemini.servicebooking.presentation.theme.Purple700
import com.krykun.movieapp.R
import com.krykun.movieapp.navigation.Screen
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun AnimatedSplashScreen(
    navHostController: NavHostController,
    viewModel: SplashScreenViewModel
) {

    val alphaAnim = animateFloatAsState(
        targetValue = if (viewModel.startAnimFlag.value) 1f else 0f,
        animationSpec = tween(durationMillis = 1000)
    )
    viewModel.collectSideEffect {
        handleSideEffect(it, navHostController)
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.startAnimFlag.value = true
    }
    Splash(alphaAnim)
}

private fun handleSideEffect(
    sideEffect: SplashScreenSideEffect,
    navHostController: NavHostController
) {
    when (sideEffect) {
        is SplashScreenSideEffect.MoveToNextScreen -> {
            navHostController.popBackStack()
            navHostController.navigate(Screen.Home().route)
        }
    }
}

@Composable
fun Splash(alpha: State<Float>) {
    Box(
        modifier = Modifier
            .background(Purple700)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.app_title),
                modifier = Modifier
                    .alpha(alpha.value)
                    .padding(top = 20.dp),
                color = Color.White,
                fontSize = 30.sp
            )
        }
    }
}
