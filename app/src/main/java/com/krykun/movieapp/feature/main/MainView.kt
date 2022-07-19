package com.krykun.movieapp.feature.main

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.krykun.movieapp.R
import com.krykun.movieapp.navigation.MainNavigation
import com.krykun.movieapp.navigation.Screen

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainView(
    navController: NavHostController = rememberAnimatedNavController()
) {
    val isBottomBarVisible = rememberSaveable {
        mutableStateOf(false)
    }
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    isBottomBarVisible.value = navBackStackEntry?.destination?.route?.contains("bottom") == true

    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = isBottomBarVisible.value,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it }),
            ) {
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
                                Screen.Home(),
                                Screen.Search(),
                                Screen.Favourite()
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
//                .padding(innerPadding)
                .background(
                    color = colorResource(id = R.color.container_background)
                ),
        ) {
            MainNavigation(
                navController = navController,
                innerPadding = innerPadding
            )
        }
    }
}