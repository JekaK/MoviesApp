package com.krykun.domain.usecase.local

import com.krykun.domain.repositories.local.MoviesLocalRepo
import javax.inject.Inject

class CheckIsMovieAddedUseCase @Inject constructor(
    private val moviesLocalDataSource: MoviesLocalRepo,
) {

    fun checkIsMovieInPlaylist(movieId: Int, playlistId: Long) =
        moviesLocalDataSource.isMovieAdded(movieId,
        playlistId)
}