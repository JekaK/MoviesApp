package com.krykun.data.mappers.remote

import com.krykun.data.model.remote.search.KnownFor
import com.krykun.data.model.remote.search.SearchItem
import com.krykun.domain.model.remote.Genre
import com.krykun.domain.model.remote.search.MediaType

object SearchMapper {

    fun SearchItem.toSearchItem(genreList: List<Genre>): com.krykun.domain.model.remote.search.SearchItem {
        return com.krykun.domain.model.remote.search.SearchItem(
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

    private fun KnownFor.toKnowFor(genreList: List<Genre>): com.krykun.domain.model.remote.search.KnownFor {
        return com.krykun.domain.model.remote.search.KnownFor(
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