package com.krykun.data.mappers

import com.krykun.data.mappers.GenresMapper.toGenre
import com.krykun.data.model.movielistitem.MovieItem
import com.krykun.domain.model.Genre
import com.krykun.domain.model.MovieDiscoverItem

object DiscoverMoviesMapper {

    fun MovieItem.toMovieDiscoverItem(
        genres: List<Genre>
    ): MovieDiscoverItem {
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
            voteCount = voteCount,
            mappedGenreIds = genreIds
                ?.map {
                    it?.toGenre(genres) ?: ""
                } ?: listOf()
        )
    }
}