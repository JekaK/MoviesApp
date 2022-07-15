package com.krykun.domain.usecase.moviedetails

import com.krykun.domain.repositories.MoviesRemoteRepo
import javax.inject.Inject

class GetMovieDetailsUseCase @Inject constructor(private val moviesRemoteRepo: MoviesRemoteRepo) {

    suspend fun getMovieDetail(
        movieId: Int
    ) = moviesRemoteRepo.getMovieDetails(
        movieId = movieId,
    )
}