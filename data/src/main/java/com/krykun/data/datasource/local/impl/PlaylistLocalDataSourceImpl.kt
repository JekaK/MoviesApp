package com.krykun.data.datasource.local.impl

import com.krykun.data.dao.PlaylistDao
import com.krykun.data.datasource.local.PlaylistLocalDataSource
import com.krykun.data.model.local.Playlist
import com.krykun.data.model.local.PlaylistMovieCrossRef
import com.krykun.data.model.local.PlaylistWithMovies
import kotlinx.coroutines.flow.Flow

class PlaylistLocalDataSourceImpl(private val playlistDao: PlaylistDao) :
    PlaylistLocalDataSource {
    override fun insertPlaylist(playlist: Playlist) {
        playlistDao.insertPlaylist(playlist)
    }

    override fun insertPlaylistMovieCrossRef(crossRef: PlaylistMovieCrossRef) {
        playlistDao.insertPlaylistMovieCrossRef(crossRef)
    }

    override fun getPlaylistsWithMovies(): Flow<List<PlaylistWithMovies>> {
        return playlistDao.getPlaylistsWithMovies()
    }

    override fun getPlaylistsWithMoviesById(playlistId: Long): Flow<PlaylistWithMovies> {
        return playlistDao.getPlaylistsWithMoviesById(playlistId)
    }
}