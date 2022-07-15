package com.krykun.domain.usecase.persondetails

import com.krykun.domain.repositories.MoviesRemoteRepo
import javax.inject.Inject

class GetPersonDetailsUseCase @Inject constructor(private val moviesRemoteRepo: MoviesRemoteRepo) {

    suspend fun getPersonDetails(movieId: Int) =
        moviesRemoteRepo.getPersonDetails(personId = movieId)
}