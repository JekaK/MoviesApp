package com.krykun.data.mappers

import com.krykun.data.model.persondetails.PersonDetailsResponse
import com.krykun.domain.model.persondetails.PersonDetails

object PersonDetailsMapper {

    fun PersonDetailsResponse.toPersonDetails(): PersonDetails {
        return PersonDetails(
            adult = adult,
            alsoKnownAs = alsoKnownAs,
            biography = biography,
            birthday = birthday,
            deathday = deathday,
            gender = gender,
            homepage = homepage,
            id = id,
            imdbId = imdbId,
            knownForDepartment = knownForDepartment,
            name = name,
            placeOfBirth = placeOfBirth,
            popularity = popularity,
            profilePath = profilePath,
        )
    }
}