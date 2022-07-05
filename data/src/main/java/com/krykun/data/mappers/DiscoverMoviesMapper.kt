package com.krykun.data.mappers

import com.krykun.data.model.movielistitem.MovieItem
import com.krykun.domain.model.MovieDiscoverItem

object DiscoverMoviesMapper {

    fun MovieItem.toMovieDiscoverItem(): MovieDiscoverItem {
        return MovieDiscoverItem(
            adult = adult,
            backdropPath = backdropPath,
            genreIds = genreIds,
            id = id,
            originalLanguage = originalLanguage,
            originalTitle = originalTitle,
            overview = overview,
            popularity = popularity,
            posterPath = posterPath,
            releaseDate = releaseDate,
            title = title,
            video = video,
            voteAverage = voteAverage,
            voteCount = voteCount
        )
    }
}