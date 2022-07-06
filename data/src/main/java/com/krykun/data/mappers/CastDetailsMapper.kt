package com.krykun.data.mappers

import com.krykun.data.model.castdetails.CastDetailsResponse
import com.krykun.domain.model.castdetails.Cast
import com.krykun.domain.model.castdetails.CastDetails
import com.krykun.domain.model.castdetails.Crew

object CastDetailsMapper {

    fun CastDetailsResponse.toCastDetails(): CastDetails {
        return CastDetails(
            castAndCrew = cast?.map {
                Cast(
                    adult = it?.adult,
                    castId = it?.castId,
                    character = it?.character,
                    creditId = it?.creditId,
                    gender = it?.gender,
                    id = it?.id,
                    knownForDepartment = it?.knownForDepartment,
                    name = it?.name,
                    order = it?.order,
                    originalName = it?.originalName,
                    popularity = it?.popularity,
                    profilePath = it?.profilePath,
                )
            } ?: (listOf<Cast>() + crew?.map {
                Crew(
                    adult = it?.adult,
                    creditId = it?.creditId,
                    department = it?.department,
                    gender = it?.gender,
                    id = it?.id,
                    job = it?.job,
                    knownForDepartment = it?.knownForDepartment,
                    name = it?.name,
                    originalName = it?.originalName,
                    popularity = it?.popularity,
                    profilePath = it?.profilePath,
                )
            })
        )
    }
}