package com.krykun.data.mappers

import com.krykun.data.mappers.GenresMapper.toGenre
import com.krykun.data.model.moviedetails.MovieDetailsResponse
import com.krykun.domain.model.Genre
import com.krykun.domain.model.moviedetails.MovieDetails

object MovieDetailMapper {
    fun MovieDetailsResponse.toMovieDetails(): MovieDetails {
        return MovieDetails(
            adult = adult,
            backdropPath = backdropPath,
            belongsToCollection = belongsToCollection,
            budget = budget,
            genres = this.genres,
            homepage = homepage,
            id = id,
            imdbId = imdbId,
            originalLanguage = originalLanguage,
            originalTitle = originalTitle,
            overview = overview,
            popularity = popularity,
            posterPath = posterPath,
            productionCompanies = productionCompanies,
            productionCountries = productionCountries,
            releaseDate = releaseDate,
            revenue = revenue,
            runtime = runtime,
            spokenLanguages = spokenLanguages,
            status = status,
            tagline = tagline,
            title = title,
            video = video,
            voteAverage = voteAverage,
            voteCount = voteCount,
        )
    }
}