package com.krykun.data.repo

import com.krykun.data.datasource.local.PlaylistLocalDataSource
import com.krykun.data.mappers.local.PlaylistMapper.toPlaylist
import com.krykun.domain.model.local.Playlist
import com.krykun.domain.repositories.local.PlaylistsLocalRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PlaylistLocalRepoImpl @Inject constructor(
    private val playlistsLocalDataSource: PlaylistLocalDataSource
) : PlaylistsLocalRepo {
    override fun insertPlaylist(playlist: Playlist) {
        playlistsLocalDataSource.insertPlaylist(playlist.toPlaylist())
    }

    override fun getAllPlaylists(): Flow<List<Playlist>> {
        return playlistsLocalDataSource.getPlaylistsWithMovies()
            .map {
                it.map {
                    it.toPlaylist()
                }
            }
    }

    override fun getPlaylistById(playlistId: Long): Flow<Playlist> {
        return playlistsLocalDataSource.getPlaylistsWithMoviesById(playlistId)
            .map {
                it.toPlaylist()
            }
    }

}