package com.krykun.data.model.moviecastdetails

import com.google.gson.annotations.SerializedName

data class CastDetailsResponse(
    @SerializedName("cast")
    val cast: List<Cast?>? = null,
    @SerializedName("crew")
    val crew: List<Crew?>? = null,
    @SerializedName("id")
    val id: Int? = null
)