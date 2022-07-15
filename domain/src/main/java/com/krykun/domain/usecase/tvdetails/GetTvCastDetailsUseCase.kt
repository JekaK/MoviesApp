package com.krykun.domain.usecase.tvdetails

import com.krykun.domain.repositories.MoviesRemoteRepo
import javax.inject.Inject

class GetTvCastDetailsUseCase @Inject constructor(private val moviesRemoteRepo: MoviesRemoteRepo) {

    suspend fun getTvCastDetails(
        tvId: Int
    ) = moviesRemoteRepo.getTvSeriesCastDetail(
        tvId = tvId,
    )
}