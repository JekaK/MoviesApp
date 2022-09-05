package com.krykun.domain.usecase.remote.moviedetails

import com.krykun.domain.repositories.remote.MoviesRemoteRepo
import javax.inject.Inject

class GetRecommendationsUseCase @Inject constructor(private val moviesRemoteRepo: MoviesRemoteRepo) {

    fun getRecommendations(
        movieId: Int,
    ) = moviesRemoteRepo.getMovieRecommendations(
        movieId = movieId,
    )
}