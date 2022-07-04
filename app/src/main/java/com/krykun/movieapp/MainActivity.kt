package com.krykun.movieapp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.krykun.movieapp.feature.main.MainView
import com.krykun.movieapp.theme.DiscoverAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @OptIn(ExperimentalMaterialNavigationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DiscoverAppTheme {
                MainView()
            }
        }
    }
}