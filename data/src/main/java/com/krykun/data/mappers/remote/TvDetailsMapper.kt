package com.krykun.data.mappers.remote

import com.krykun.data.model.remote.tvcastdetails.TvCastDetailsResponse
import com.krykun.data.model.remote.tvdetails.TvDetailsResponse
import com.krykun.domain.model.remote.tvcastdetails.Cast
import com.krykun.domain.model.remote.tvcastdetails.Crew
import com.krykun.domain.model.remote.tvcastdetails.TvCastDetails
import com.krykun.domain.model.remote.tvdetails.*
import java.text.SimpleDateFormat
import java.util.*

object TvDetailsMapper {

    fun TvDetailsResponse.toTvDetails(): TvDetails {
        return TvDetails(
            adult = adult,
            backdropPath = backdropPath,
            createdBy = createdBy?.map {
                CreatedBy(
                    creditId = it?.creditId,
                    gender = it?.gender,
                    id = it?.id,
                    name = it?.name,
                    profilePath = it?.profilePath,
                )
            },
            episodeRunTime = episodeRunTime?.map {
                it
            },
            firstAirDate = if (firstAirDate?.isNotEmpty() == true) {
                val calendar = Calendar.getInstance(TimeZone.getDefault())
                calendar.time = SimpleDateFormat("yyyy-MM-dd").parse(
                    firstAirDate
                )
                calendar.get(Calendar.YEAR).toString()
            } else {
                ""
            },
            genres = genres?.map {
                Genre(
                    id = it?.id,
                    name = it?.name,
                )
            },
            homepage = homepage,
            id = id,
            inProduction = inProduction,
            languages = languages?.map {
                it
            },
            lastAirDate = lastAirDate,
            lastEpisodeToAir = LastEpisodeToAir(
                airDate = lastEpisodeToAir?.airDate,
                episodeNumber = lastEpisodeToAir?.episodeNumber,
                id = lastEpisodeToAir?.id,
                name = lastEpisodeToAir?.name,
                overview = lastEpisodeToAir?.overview,
                productionCode = lastEpisodeToAir?.productionCode,
                runtime = lastEpisodeToAir?.runtime,
                seasonNumber = lastEpisodeToAir?.seasonNumber,
                stillPath = lastEpisodeToAir?.stillPath,
                voteAverage = lastEpisodeToAir?.voteAverage,
                voteCount = lastEpisodeToAir?.voteCount,
            ),
            name = name,
            networks = networks?.map {
                Network(
                    id = it?.id,
                    logoPath = it?.logoPath,
                    name = it?.name,
                    originCountry = it?.originCountry,
                )
            },
            nextEpisodeToAir = nextEpisodeToAir,
            numberOfEpisodes = numberOfEpisodes,
            numberOfSeasons = numberOfSeasons,
            originCountry = originCountry,
            originalLanguage = originalLanguage,
            originalName = originalName,
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
            seasons = seasons?.map {
                Season(
                    airDate = it?.airDate,
                    episodeCount = it?.episodeCount,
                    id = it?.id,
                    name = it?.name,
                    overview = it?.overview,
                    posterPath = it?.posterPath,
                    seasonNumber = it?.seasonNumber,
                )
            },
            spokenLanguages = spokenLanguages?.map {
                SpokenLanguage(
                    englishName = it?.englishName,
                    iso6391 = it?.iso6391,
                    name = it?.name,
                )
            },
            status = status,
            tagline = tagline,
            type = type,
            voteAverage = voteAverage,
            voteCount = voteCount,
        )
    }

    fun TvCastDetailsResponse.toTvCastDetails(): TvCastDetails {
        return TvCastDetails(
            castAndCrew = cast?.map {
                Cast(
                    adult = it?.adult,
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