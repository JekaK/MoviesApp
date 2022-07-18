package com.krykun.data.mappers.remote

import com.krykun.data.model.remote.persondetails.PersonDetailsResponse
import com.krykun.domain.model.remote.persondetails.PersonDetails

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