package com.krykun.domain.usecase.local

import com.krykun.domain.repositories.local.PlaylistsLocalRepo
import javax.inject.Inject

class GetPlaylistMoviesByLimit @Inject constructor(
    private val playlistsLocalRepo: PlaylistsLocalRepo
) {

    fun getAllPlaylistsWithLimit(amount: Int) =
        playlistsLocalRepo.getAllPlaylistsWithMoviesByLimit(amount)
}