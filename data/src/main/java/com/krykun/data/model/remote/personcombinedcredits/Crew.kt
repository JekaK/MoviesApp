package com.krykun.data.model.remote.personcombinedcredits


import com.google.gson.annotations.SerializedName

data class Crew(
    @SerializedName("adult")
    val adult: Boolean? = null,
    @SerializedName("backdrop_path")
    val backdropPath: Any? = null,
    @SerializedName("credit_id")
    val creditId: String? = null,
    @SerializedName("department")
    val department: String? = null,
    @SerializedName("genre_ids")
    val genreIds: List<Int?>? = null,
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("job")
    val job: String? = null,
    @SerializedName("media_type")
    val mediaType: String? = null,
    @SerializedName("original_language")
    val originalLanguage: String? = null,
    @SerializedName("original_title")
    val originalTitle: String? = null,
    @SerializedName("overview")
    val overview: String? = null,
    @SerializedName("popularity")
    val popularity: Double? = null,
    @SerializedName("poster_path")
    val posterPath: Any? = null,
    @SerializedName("release_date")
    val releaseDate: String? = null,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("video")
    val video: Boolean? = null,
    @SerializedName("vote_average")
    val voteAverage: Double? = null,
    @SerializedName("vote_count")
    val voteCount: Double? = null
)