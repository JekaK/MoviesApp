package com.krykun.domain.usecase.remote.moviedetails

import com.krykun.domain.model.remote.Genre
import com.krykun.domain.repositories.remote.MoviesRemoteRepo
import javax.inject.Inject

class GetRecommendationsUseCase @Inject constructor(private val moviesRemoteRepo: MoviesRemoteRepo) {

    fun getRecommendations(
        movieId: Int,
        genres: List<Genre>
    ) = moviesRemoteRepo.getMovieRecommendations(
        movieId = movieId,
        genres = genres
    )

}