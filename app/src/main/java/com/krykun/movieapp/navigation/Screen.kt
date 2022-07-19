package com.krykun.movieapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    open val title: String = "",
    open val icon: ImageVector = Icons.Default.Home,
    open val route: String
) {
    data class Home(
        override val title: String = "Home",
        override val icon: ImageVector = Icons.Filled.Home,
        override val route: String = "home_bottom_screen"
    ) : Screen(route = route)

    data class Search(
        override val title: String = "Search",
        override val icon: ImageVector = Icons.Filled.Search,
        override val route: String = "search_bottom_screen"
    ) : Screen(route = route)

    data class Favourite(
        override val title: String = "Favourite",
        override val icon: ImageVector = Icons.Filled.Favorite,
        override val route: String = "favourite_bottom_screen"
    ) : Screen(route = route)

    object Splash : Screen(route = "splash_screen")

    data class MovieDetails(
        override val route: String = "movie_details_screen",
    ) : Screen(route = route)

    data class TvSeriesDetails(
        override val route: String = "tv_series_details_screen",
    ) : Screen(route = route)

    data class PersonDetails(
        override val route: String = "person_details_screen",
    ) : Screen(route = route)
}
