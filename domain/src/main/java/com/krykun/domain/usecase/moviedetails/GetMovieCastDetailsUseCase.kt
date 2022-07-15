package com.krykun.domain.usecase.moviedetails

import com.krykun.domain.repositories.MoviesRemoteRepo
import javax.inject.Inject

class GetMovieCastDetailsUseCase @Inject constructor(private val moviesRemoteRepo: MoviesRemoteRepo) {

    suspend fun getMovieCastDetails(
        movieId: Int
    ) = moviesRemoteRepo.getMovieCastDetails(
        movieId = movieId,
    )
}