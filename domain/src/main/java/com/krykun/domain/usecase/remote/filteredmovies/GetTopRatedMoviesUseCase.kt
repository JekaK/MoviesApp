package com.krykun.domain.usecase.remote.filteredmovies

import com.krykun.domain.model.remote.Genre
import com.krykun.domain.repositories.remote.MoviesRemoteRepo
import javax.inject.Inject

class GetTopRatedMoviesUseCase @Inject constructor(
    private val moviesRemoteRepo: MoviesRemoteRepo
) {

    fun getTopRatedMovies(genres: List<Genre>) = moviesRemoteRepo.getTopRatedMovies(genres)

}