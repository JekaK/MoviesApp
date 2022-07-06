package com.krykun.data.mappers

import com.krykun.data.model.moviedetails.MovieDetailsResponse
import com.krykun.domain.model.moviedetails.*

object MovieDetailMapper {
    fun MovieDetailsResponse.toMovieDetails(): MovieDetails {
        return MovieDetails(
            adult = adult,
            backdropPath = backdropPath,
            belongsToCollection = BelongsToCollection(
                backdropPath = belongsToCollection?.backdropPath,
                id = belongsToCollection?.id,
                name = belongsToCollection?.name,
                posterPath = belongsToCollection?.posterPath
            ),
            budget = budget,
            genres = genres?.map {
                Genre(
                    id = it?.id,
                    name = it?.name
                )
            },
            homepage = homepage,
            id = id,
            imdbId = imdbId,
            originalLanguage = originalLanguage,
            originalTitle = originalTitle,
            overview = overview,
            popularity = popularity,
            posterPath = posterPath,
            productionCompanies = productionCompanies?.map {
                ProductionCompany(
                    id = it?.id,
                    logoPath = it?.logoPath,
                    name = it?.name,
                    originCountry = it?.originCountry
                )
            },
            productionCountries = productionCountries?.map {
                ProductionCountry(
                    iso31661 = it?.iso31661,
                    name = it?.name,
                )
            },
            releaseDate = releaseDate,
            revenue = revenue,
            runtime = runtime,
            spokenLanguages = spokenLanguages?.map {
                SpokenLanguage(
                    englishName = it?.englishName,
                    iso6391 = it?.iso6391,
                    name = it?.name
                )
            },
            status = status,
            tagline = tagline,
            title = title,
            video = video,
            voteAverage = voteAverage,
            voteCount = voteCount,
        )
    }
}