package com.krykun.domain.usecase.remote.moviedetails

import com.krykun.domain.model.remote.moviedetails.MovieDetails
import javax.inject.Inject

class GetMovieDetailsAndCastUseCase @Inject constructor(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val getMovieCastUseCase: GetMovieCastDetailsUseCase
) {

    suspend fun getMovieDetailsAndCast(movieId: Int): Result<MovieDetails>? {
        val castResult =
            getMovieCastUseCase.getMovieCastDetails(movieId = movieId)
        val result =
            getMovieDetailsUseCase.getMovieDetail(movieId = movieId)
        return if (result.isSuccess && castResult.isSuccess) {
            val verifiedResponse = result.map {
                it.copy(
                    cast = castResult.getOrNull()
                )
            }
            verifiedResponse
        } else {
            null
        }
    }
}