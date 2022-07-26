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
    override suspend fun insertPlaylist(playlist: Playlist): Long =
        playlistsLocalDataSource.insertPlaylist(playlist.toPlaylist())


    override fun getAllPlaylistsFlow(): Flow<List<Playlist>> {
        return playlistsLocalDataSource.getPlaylistsWithMoviesFlow()
            .map {
                it.map {
                    it.toPlaylist()
                }
            }
    }

    override fun getAllPlaylists(): List<Playlist> {
        return playlistsLocalDataSource.getPlaylistsWithMovies()
            .map {
                it.toPlaylist()
            }
    }

    override fun getPlaylistById(playlistId: Long): Flow<Playlist> {
        return playlistsLocalDataSource.getPlaylistsWithMoviesById(playlistId)
            .map {
                it.toPlaylist()
            }
    }

    override fun getAllPlaylistsWithMoviesByLimit(amount: Int): Flow<List<Playlist>> {
        return playlistsLocalDataSource.getAllPlaylistsWithMoviesByLimit(amount)
            .map {
                it.map {
                    it.toPlaylist()
                }
            }
    }

}