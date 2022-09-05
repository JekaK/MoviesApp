package com.krykun.data.mappers.remote

import android.annotation.SuppressLint
import com.krykun.data.model.remote.moviecastdetails.CastDetailsResponse
import com.krykun.data.model.remote.moviedetails.MovieDetailsResponse
import com.krykun.domain.model.remote.moviecastdetails.Cast
import com.krykun.domain.model.remote.moviecastdetails.CastDetails
import com.krykun.domain.model.remote.moviecastdetails.Crew
import com.krykun.domain.model.remote.moviedetails.*
import java.text.SimpleDateFormat
import java.util.*

object MovieDetailMapper {
    @SuppressLint("SimpleDateFormat")
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
            releaseDate = if (releaseDate?.isNotEmpty() == true) {
                val calendar = Calendar.getInstance(TimeZone.getDefault())
                calendar.time = SimpleDateFormat("yyyy-MM-dd").parse(
                    releaseDate
                ) as Date
                calendar.get(Calendar.YEAR).toString()
            } else {
                ""
            },
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

    fun CastDetailsResponse.toCastDetails(): CastDetails {
        return CastDetails(
            castAndCrew = cast?.map {
                Cast(
                    adult = it?.adult,
                    castId = it?.castId,
                    character = it?.character,
                    creditId = it?.creditId,
                    gender = it?.gender,
                    id = it?.id,
                    knownForDepartment = it?.knownForDepartment,
                    name = it?.name,
                    order = it?.order,
                    originalName = it?.originalName,
                    popularity = it?.popularity,
                    profilePath = it?.profilePath,
                )
            } ?: (listOf<Cast>() + crew?.map {
                Crew(
                    adult = it?.adult,
                    creditId = it?.creditId,
                    department = it?.department,
                    gender = it?.gender,
                    id = it?.id,
                    job = it?.job,
                    knownForDepartment = it?.knownForDepartment,
                    name = it?.name,
                    originalName = it?.originalName,
                    popularity = it?.popularity,
                    profilePath = it?.profilePath,
                )
            })
        )
    }
}