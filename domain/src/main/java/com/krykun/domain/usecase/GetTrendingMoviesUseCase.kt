package com.krykun.domain.usecase

import com.krykun.domain.model.Genre
import com.krykun.domain.repositories.MoviesRemoteRepo
import javax.inject.Inject

class GetTrendingMoviesUseCase @Inject constructor(
    private val moviesRemoteRepo: MoviesRemoteRepo
) {
    fun getTrendingMovies(
        genres: List<Genre>
    ) = moviesRemoteRepo.getTrendingMovies(genres)
}