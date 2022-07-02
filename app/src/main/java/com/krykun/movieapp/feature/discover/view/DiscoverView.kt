package com.krykun.movieapp.feature.discover.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavHostController
import com.krykun.movieapp.R
import com.krykun.movieapp.feature.discover.presentation.viewmodel.DiscoverMoviesViewModel
import com.krykun.movieapp.feature.discover.presentation.viewmodel.UpcomingMoviesViewModel

@Composable
fun DiscoverView(
    viewModel: DiscoverMoviesViewModel,
    upcomingMoviesViewModel: UpcomingMoviesViewModel,
    navHostController: NavHostController
) {
    Column(
        modifier = Modifier
            .background(colorResource(id = R.color.container_background))
            .fillMaxSize()
    ) {
        UpcomingView(
            viewModel = upcomingMoviesViewModel,
            navHostController = navHostController
        )
    }
}
