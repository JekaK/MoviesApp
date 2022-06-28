package com.krykun.movieapp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.hilt.navigation.compose.hiltViewModel
import com.capgemini.servicebooking.presentation.theme.DiscoverAppTheme
import com.krykun.movieapp.feature.discovermovies.presentation.DiscoverMoviesViewModel
import com.krykun.movieapp.feature.discovermovies.view.DiscoverMoviesView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DiscoverAppTheme {
                val viewModel: DiscoverMoviesViewModel = hiltViewModel()
                DiscoverMoviesView(viewModel)
            }
        }
    }
}