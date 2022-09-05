package com.krykun.data.mappers.remote

import com.krykun.data.model.remote.movierecommendations.MovieRecommendationResponse
import com.krykun.domain.model.remote.Genre
import com.krykun.domain.model.remote.movierecommendations.MovieRecommendationItem

object RecommendationMoviesMapper {

    fun MovieRecommendationResponse.toMovieRecommendations(): MovieRecommendationItem {
        return MovieRecommendationItem(
            id = id,
            posterPath = posterPath,
            title = originalTitle,
        )
    }
}