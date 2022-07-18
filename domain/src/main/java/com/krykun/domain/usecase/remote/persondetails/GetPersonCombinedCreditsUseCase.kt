package com.krykun.domain.usecase.remote.persondetails

import com.krykun.domain.model.remote.Genre
import com.krykun.domain.repositories.remote.MoviesRemoteRepo
import javax.inject.Inject

class GetPersonCombinedCreditsUseCase @Inject constructor(private val moviesRemoteRepo: MoviesRemoteRepo) {

    suspend fun getPersonCombinedCredits(
        personId: Int,
        genres: List<Genre>
    ) = moviesRemoteRepo.getPersonCombinedCredits(personId, genres)
}