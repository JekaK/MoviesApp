package com.krykun.movieapp.feature.home.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavHostController
import com.krykun.movieapp.R
import com.krykun.movieapp.feature.discover.presentation.DiscoverMoviesViewModel
import com.krykun.movieapp.feature.discover.view.DiscoverView
import com.krykun.movieapp.feature.home.presentation.HomeMoviesViewModel
import com.krykun.movieapp.feature.trending.presentation.TrendingViewModel
import com.krykun.movieapp.feature.trending.view.TrendingView

@Composable
fun HomeView(
    viewModel: HomeMoviesViewModel,
    discoverMoviesViewModel: DiscoverMoviesViewModel,
    trendingViewModel: TrendingViewModel,
    navHostController: NavHostController
) {
    Column(
        modifier = Modifier
            .background(colorResource(id = R.color.container_background))
            .fillMaxSize()
    ) {
        DiscoverView(
            viewModel = discoverMoviesViewModel,
            navHostController = navHostController
        )

        TrendingView(
            viewModel = trendingViewModel,
            navHostController = navHostController
        )
    }
}
