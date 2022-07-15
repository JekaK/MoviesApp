package com.krykun.data.mappers

import com.krykun.data.mappers.GenresMapper.toGenre
import com.krykun.domain.model.personcombinedcredits.PersonCombinedCredits
import com.krykun.data.model.personcombinedcredits.PersonCombinedCreditsResponse
import com.krykun.domain.model.Genre
import com.krykun.domain.model.personcombinedcredits.Cast
import com.krykun.domain.model.personcombinedcredits.Crew

object PersonCombinedCreditsMapper {

    fun PersonCombinedCreditsResponse.toPersonCombinedCredits(genres: List<Genre>): PersonCombinedCredits {
        return PersonCombinedCredits(
            cast = cast?.map {
                Cast(
                    adult = it?.adult,
                    backdropPath = it?.backdropPath,
                    character = it?.character,
                    creditId = it?.creditId,
                    episodeCount = it?.episodeCount,
                    firstAirDate = it?.firstAirDate,
                    genreIds = it?.genreIds
                        ?.map {
                            it?.toGenre(genres) ?: ""
                        } ?: listOf(),
                    id = it?.id,
                    mediaType = it?.mediaType,
                    name = it?.name,
                    order = it?.order,
                    originCountry = it?.originCountry,
                    originalLanguage = it?.originalLanguage,
                    originalName = it?.originalName,
                    originalTitle = it?.originalTitle,
                    overview = it?.overview,
                    popularity = it?.popularity,
                    posterPath = it?.posterPath,
                    releaseDate = it?.releaseDate,
                    title = it?.title,
                    video = it?.video,
                    voteAverage = it?.voteAverage,
                    voteCount = it?.voteCount,
                )
            },
            crew = crew?.map {
                Crew(
                    adult = it?.adult,
                    backdropPath = it?.backdropPath,
                    creditId = it?.creditId,
                    department = it?.department,
                    genreIds = it?.genreIds
                        ?.map {
                            it?.toGenre(genres) ?: ""
                        } ?: listOf(),
                    id = it?.id,
                    job = it?.job,
                    mediaType = it?.mediaType,
                    originalLanguage = it?.originalLanguage,
                    originalTitle = it?.originalTitle,
                    overview = it?.overview,
                    popularity = it?.popularity,
                    posterPath = it?.posterPath,
                    releaseDate = it?.releaseDate,
                    title = it?.title,
                    video = it?.video,
                    voteAverage = it?.voteAverage,
                    voteCount = it?.voteCount,
                )
            },
            id = id,
        )
    }
}