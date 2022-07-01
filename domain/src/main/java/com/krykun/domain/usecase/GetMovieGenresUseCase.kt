package com.krykun.domain.usecase

import com.krykun.domain.repositories.MoviesRemoteRepo
import javax.inject.Inject

class GetMovieGenresUseCase @Inject constructor(private val moviesRemoteRepo: MoviesRemoteRepo) {

    suspend fun getMovieGenres() = moviesRemoteRepo.getGenres()
}