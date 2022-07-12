package com.krykun.domain.model.search

data class SearchItem(
    val adult: Boolean? = null,
    val backdropPath: String? = null,
    val genre: List<String?>? = null,
    val id: Int? = null,
    val originalLanguage: String? = null,
    val originalTitle: String? = null,
    val overview: String? = null,
    val popularity: Double? = null,
    val posterPath: String? = null,
    val releaseDate: String? = null,
    val title: String? = null,
    val video: Boolean? = null,
    val voteAverage: Double? = null,
    val voteCount: Int? = null,
    val gender: Int? = null,
    val knownFor: List<KnownFor?>? = null,
    val knownForDepartment: String? = null,
    val mediaType: MediaType? = null,
    val name: String? = null,
    val profilePath: String? = null
)

enum class MediaType {
    MOVIE,
    TV,
    PERSON,
    NONE
}