package com.krykun.domain.usecase.remote.tvdetails

import com.krykun.domain.repositories.remote.MoviesRemoteRepo
import javax.inject.Inject

class GetTvGenresUseCase @Inject constructor(private val moviesRemoteRepo: MoviesRemoteRepo) {

    suspend fun getTvGenres() = moviesRemoteRepo.getTvGenres()
}