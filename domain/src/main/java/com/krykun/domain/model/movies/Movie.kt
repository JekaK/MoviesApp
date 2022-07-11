package com.krykun.domain.model.movies

data class Movie(
    val adult: Boolean? = null,
    val backdropPath: String? = null,
    val genreIds: List<Int?>? = null,
    val id: Int? = null,
    val mediaType: String? = null,
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
    val mappedGenreIds: List<String> = listOf()
)