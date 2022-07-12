package com.krykun.data.mappers

import com.krykun.data.model.search.KnownFor
import com.krykun.data.model.search.SearchItem
import com.krykun.domain.model.Genre
import com.krykun.domain.model.search.MediaType

object SearchMapper {

    fun SearchItem.toSearchItem(genreList: List<Genre>): com.krykun.domain.model.search.SearchItem {
        return com.krykun.domain.model.search.SearchItem(
            adult = adult,
            backdropPath = backdropPath,
            genre = genreIds?.map {
                it?.toGenre(genreList)
            },
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
            gender = gender,
            knownFor = knownFor?.map {
                it?.toKnowFor(genreList)
            },
            knownForDepartment = knownForDepartment,
            mediaType = mediaType?.toMediaType(),
            name = name,
            profilePath = profilePath,
        )
    }

    private fun KnownFor.toKnowFor(genreList: List<Genre>): com.krykun.domain.model.search.KnownFor {
        return com.krykun.domain.model.search.KnownFor(
            adult = adult,
            backdropPath = backdropPath,
            genre = genreIds?.map {
                it?.toGenre(genreList) ?: ""
            },
            id = id,
            mediaType = mediaType,
            originalLanguage = originalLanguage,
            originalTitle = originalTitle,
            overview = originalLanguage,
            posterPath = posterPath,
            releaseDate = releaseDate,
            title = title,
            video = video,
            voteAverage = voteAverage,
            voteCount = voteCount,
        )
    }

    private fun Int.toGenre(genreList: List<Genre>): String {
        return genreList.find {
            it.id == this
        }?.name ?: ""
    }

    private fun String.toMediaType(): MediaType {
        return when (this) {
            "movie" -> {
                MediaType.MOVIE
            }
            "tv" -> {
                MediaType.TV
            }
            "person" -> {
                MediaType.PERSON
            }
            else -> {
                MediaType.NONE
            }
        }
    }
}