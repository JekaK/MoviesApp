package com.krykun.data.mappers.local

import com.krykun.data.model.local.Movie
import com.krykun.domain.model.remote.moviedetails.MovieDetails
import com.krykun.domain.model.remote.tvdetails.TvDetails

object MoviesMapper {

    fun MovieDetails.toMovie(): Movie {
        return Movie(
            movieId = id?.toLong() ?: 0,
            name = originalTitle ?: "",
            poster = posterPath ?: "",
            type = "movie",
        )
    }

    fun TvDetails.toMovie(): Movie {
        return Movie(
            movieId = id?.toLong() ?: 0,
            name = originalName ?: "",
            poster = posterPath ?: "",
            type = "tvseries",
        )
    }

    fun Movie.toMovie(): com.krykun.domain.model.local.Movie {
        return com.krykun.domain.model.local.Movie(
            movieId = movieId,
            name = name,
            poster = poster,
            type = type,
        )
    }
}