package com.krykun.movieapp.feature.main

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.krykun.movieapp.R
import com.krykun.movieapp.navigation.BottomNavigation
import com.krykun.movieapp.navigation.BottomNavGraph

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainView() {
    val navController = rememberAnimatedNavController()

    Scaffold(
        bottomBar = {
            Column(
                modifier = Modifier.background(
                    color = colorResource(id = R.color.container_background)
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(
                            start = 10.dp,
                            end = 10.dp,
                            bottom = 10.dp
                        )
                ) {
                    BottomNavigationView(
                        navController = navController,
                        listOf(
                            BottomNavGraph.Discover(),
                            BottomNavGraph.Search(),
                            BottomNavGraph.Favourite()
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .background(
                    color = colorResource(id = R.color.container_background)
                )
        ) {
            BottomNavigation(
                navController = navController
            )
        }
    }
}