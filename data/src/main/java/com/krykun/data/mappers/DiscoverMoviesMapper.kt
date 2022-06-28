package com.krykun.data.mappers

import com.krykun.data.model.MovieItem
import com.krykun.domain.model.MovieDiscoverItem
import java.text.SimpleDateFormat
import java.util.*

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