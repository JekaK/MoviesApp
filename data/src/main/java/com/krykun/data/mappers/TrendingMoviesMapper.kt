package com.krykun.data.mappers

import com.krykun.data.mappers.GenresMapper.toGenre
import com.krykun.data.model.trending.TrendingMovieItemResponse
import com.krykun.domain.model.Genre
import com.krykun.domain.model.trending.TrendingMovie

object TrendingMoviesMapper {

    fun TrendingMovieItemResponse.toTrendingMovie(
        genres: List<Genre>
    ): TrendingMovie {
        return TrendingMovie(
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