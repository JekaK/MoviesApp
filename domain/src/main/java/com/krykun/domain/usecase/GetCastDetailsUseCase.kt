package com.krykun.domain.usecase

import com.krykun.domain.repositories.MoviesRemoteRepo
import javax.inject.Inject

class GetCastDetailsUseCase @Inject constructor(private val moviesRemoteRepo: MoviesRemoteRepo) {

    suspend fun getCastDetails(
        movieId: Int
    ) = moviesRemoteRepo.getCastDetails(
        movieId = movieId,
    )
}