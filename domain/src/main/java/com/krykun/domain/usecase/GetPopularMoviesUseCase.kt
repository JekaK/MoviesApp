package com.krykun.domain.usecase

import com.krykun.domain.model.Genre
import com.krykun.domain.repositories.MoviesRemoteRepo
import javax.inject.Inject

class GetPopularMoviesUseCase @Inject constructor(
    private val moviesRemoteRepo: MoviesRemoteRepo
) {
    fun getPopularMovies(genres: List<Genre>) = moviesRemoteRepo.getPopularMovies(genres)
}