package com.krykun.domain.usecase.local

import com.krykun.domain.model.local.Playlist
import com.krykun.domain.repositories.local.PlaylistsLocalRepo
import javax.inject.Inject

class AddPlaylistUseCase @Inject constructor(
    private val playlistsLocalRepo: PlaylistsLocalRepo
) {

    suspend fun addPlaylist(playlist: Playlist) = playlistsLocalRepo.insertPlaylist(playlist)
}