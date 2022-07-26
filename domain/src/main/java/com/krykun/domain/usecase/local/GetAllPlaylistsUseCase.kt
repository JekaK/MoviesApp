package com.krykun.domain.usecase.local

import com.krykun.domain.repositories.local.PlaylistsLocalRepo
import javax.inject.Inject

class GetAllPlaylistsUseCase @Inject constructor(
    private val playlistsLocalRepo: PlaylistsLocalRepo
) {

    fun getAllPlaylistsFlow() = playlistsLocalRepo.getAllPlaylistsFlow()

    fun getAllPlaylists() = playlistsLocalRepo.getAllPlaylists()
}