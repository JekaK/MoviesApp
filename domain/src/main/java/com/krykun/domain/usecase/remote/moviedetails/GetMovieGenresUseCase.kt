package com.krykun.domain.usecase.remote.moviedetails

import com.krykun.domain.repositories.remote.MoviesRemoteRepo
import javax.inject.Inject

class GetMovieGenresUseCase @Inject constructor(private val moviesRemoteRepo: MoviesRemoteRepo) {

    suspend fun getMovieGenres() = moviesRemoteRepo.getGenres()
}