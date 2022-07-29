package com.krykun.domain.usecase.local

import com.krykun.domain.repositories.local.PlaylistsLocalRepo
import javax.inject.Inject

class RemovePlaylistUseCase @Inject constructor(
    private val playlistsLocalRepo: PlaylistsLocalRepo
) {

    suspend fun removePlaylist(playlistId: Long) = playlistsLocalRepo.removePlaylist(playlistId)
}