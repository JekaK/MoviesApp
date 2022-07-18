package com.krykun.domain.usecase.remote.moviedetails

import com.krykun.domain.repositories.remote.MoviesRemoteRepo
import javax.inject.Inject

class GetMovieCastDetailsUseCase @Inject constructor(private val moviesRemoteRepo: MoviesRemoteRepo) {

    suspend fun getMovieCastDetails(
        movieId: Int
    ) = moviesRemoteRepo.getMovieCastDetails(
        movieId = movieId,
    )
}