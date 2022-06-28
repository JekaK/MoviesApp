package com.krykun.movieapp.feature.discovermovies.view

import android.annotation.SuppressLint
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.paging.compose.collectAsLazyPagingItems
import com.krykun.movieapp.feature.discovermovies.presentation.DiscoverMoviesViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun DiscoverMoviesView(viewModel: DiscoverMoviesViewModel) {
    val movies = viewModel.getDiscoverMovies.collectAsLazyPagingItems()
    Scaffold {
        DiscoverMoviesContent(allMovies = movies)
    }
}