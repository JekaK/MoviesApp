package com.krykun.domain.usecase.remote.filteredmovies

import com.krykun.domain.model.remote.Genre
import com.krykun.domain.repositories.remote.MoviesRemoteRepo
import javax.inject.Inject

class GetTrendingMoviesUseCase @Inject constructor(
    private val moviesRemoteRepo: MoviesRemoteRepo
) {
    fun getTrendingMovies(genres: List<Genre>) = moviesRemoteRepo.getTrendingMovies(genres)
}