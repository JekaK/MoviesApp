package com.krykun.domain.model.persondetails

import com.krykun.domain.model.personcombinedcredits.PersonCombinedCredits

data class PersonDetails(
    val adult: Boolean? = null,
    val alsoKnownAs: List<String?>? = null,
    val biography: String? = null,
    val birthday: String? = null,
    val deathday: Any? = null,
    val gender: Int? = null,
    val homepage: Any? = null,
    val id: Int? = null,
    val imdbId: String? = null,
    val knownForDepartment: String? = null,
    val name: String? = null,
    val placeOfBirth: String? = null,
    val popularity: Double? = null,
    val profilePath: String? = null,
    val personCombinedCredits: PersonCombinedCredits? = null
)