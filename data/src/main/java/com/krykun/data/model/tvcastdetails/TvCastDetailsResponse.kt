package com.krykun.data.model.tvcastdetails

import com.google.gson.annotations.SerializedName

data class TvCastDetailsResponse(
    @SerializedName("cast")
    val cast: List<Cast?>? = null,
    @SerializedName("crew")
    val crew: List<Crew?>? = null,
    @SerializedName("id")
    val id: Int? = null
)