package com.krykun.data.model.remote.search


import com.google.gson.annotations.SerializedName

data class SearchItem(
    @SerializedName("adult")
    val adult: Boolean? = null,
    @SerializedName("backdrop_path")
    val backdropPath: String? = null,
    @SerializedName("genre_ids")
    val genreIds: List<Int?>? = null,
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("original_language")
    val originalLanguage: String? = null,
    @SerializedName("original_title")
    val originalTitle: String? = null,
    @SerializedName("overview")
    val overview: String? = null,
    @SerializedName("popularity")
    val popularity: Double? = null,
    @SerializedName("poster_path")
    val posterPath: String? = null,
    @SerializedName("release_date")
    val releaseDate: String? = null,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("video")
    val video: Boolean? = null,
    @SerializedName("vote_average")
    val voteAverage: Double? = null,
    @SerializedName("vote_count")
    val voteCount: Int? = null,
    @SerializedName("gender")
    val gender: Int? = null,
    @SerializedName("known_for")
    val knownFor: List<KnownFor?>? = null,
    @SerializedName("known_for_department")
    val knownForDepartment: String? = null,
    @SerializedName("media_type")
    val mediaType: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("profile_path")
    val profilePath: String? = null
)