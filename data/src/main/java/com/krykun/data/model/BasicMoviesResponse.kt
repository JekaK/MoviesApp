package com.krykun.data.model


import com.google.gson.annotations.SerializedName

data class BasicMoviesResponse<T>(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<T>? = null,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)