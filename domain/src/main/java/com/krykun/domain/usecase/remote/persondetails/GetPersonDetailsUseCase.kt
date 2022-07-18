package com.krykun.domain.usecase.remote.persondetails

import com.krykun.domain.repositories.remote.MoviesRemoteRepo
import javax.inject.Inject

class GetPersonDetailsUseCase @Inject constructor(private val moviesRemoteRepo: MoviesRemoteRepo) {

    suspend fun getPersonDetails(movieId: Int) =
        moviesRemoteRepo.getPersonDetails(personId = movieId)
}