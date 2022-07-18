package com.krykun.data.model.remote.personcombinedcredits


import com.google.gson.annotations.SerializedName

data class PersonCombinedCreditsResponse(
    @SerializedName("cast")
    val cast: List<Cast?>? = null,
    @SerializedName("crew")
    val crew: List<Crew?>? = null,
    @SerializedName("id")
    val id: Int? = null
)