package com.krykun.data.mappers.remote

import com.krykun.data.mappers.remote.GenresMapper.toGenre
import com.krykun.data.model.remote.movies.MovieItemResponse
import com.krykun.domain.model.remote.Genre
import com.krykun.domain.model.remote.movies.Movie

object MoviesMapper {

    fun MovieItemResponse.toMovie(
        genres: List<Genre>
    ): Movie {
        return Movie(
            adult = adult,
            backdropPath = backdropPath,
            genreIds = genreIds,
            id = id,
            mediaType = mediaType,
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