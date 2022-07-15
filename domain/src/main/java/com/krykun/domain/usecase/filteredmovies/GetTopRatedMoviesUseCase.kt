package com.krykun.domain.usecase.filteredmovies

import com.krykun.domain.model.Genre
import com.krykun.domain.repositories.MoviesRemoteRepo
import javax.inject.Inject

class GetTopRatedMoviesUseCase @Inject constructor(
    private val moviesRemoteRepo: MoviesRemoteRepo
) {

    fun getTopRatedMovies(genres: List<Genre>) = moviesRemoteRepo.getTopRatedMovies(genres)

}