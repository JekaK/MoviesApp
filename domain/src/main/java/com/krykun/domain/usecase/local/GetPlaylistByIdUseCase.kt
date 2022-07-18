package com.krykun.domain.usecase.local

import com.krykun.domain.repositories.local.PlaylistsLocalRepo
import javax.inject.Inject

class GetPlaylistByIdUseCase @Inject constructor(
    private val playlistsLocalRepo: PlaylistsLocalRepo
) {

    fun getPlaylistById(playlistId: Long) = playlistsLocalRepo.getPlaylistById(playlistId)
}