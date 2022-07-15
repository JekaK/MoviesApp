package com.krykun.domain.usecase.persondetails

import com.krykun.domain.model.Genre
import com.krykun.domain.repositories.MoviesRemoteRepo
import javax.inject.Inject

class GetPersonCombinedCreditsUseCase @Inject constructor(private val moviesRemoteRepo: MoviesRemoteRepo) {

    suspend fun getPersonCombinedCredits(
        personId: Int,
        genres: List<Genre>
    ) = moviesRemoteRepo.getPersonCombinedCredits(personId, genres)
}