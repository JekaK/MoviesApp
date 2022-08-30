package com.krykun.movieapp.feature.moviedetails.view

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import com.krykun.domain.model.remote.moviecastdetails.Cast
import com.krykun.domain.model.remote.moviecastdetails.Crew
import com.krykun.movieapp.feature.moviedetails.presentation.MovieDetailsViewModel

@Composable
fun CastAndCrewView(viewModel: MovieDetailsViewModel) {
    LazyRow {
        itemsIndexed(
            viewModel.movieData.value?.cast?.castAndCrew ?: listOf()
        ) { index, item ->
            if (item is Cast) {
                CastView(castItem = item)
            } else {
                CrewView(crewItem = item as Crew)
            }
        }
    }
}