package com.krykun.data.di

import com.krykun.data.api.ApiService
import com.krykun.data.dao.MovieDao
import com.krykun.data.dao.PlaylistDao
import com.krykun.data.datasource.local.MoviesLocalDataSource
import com.krykun.data.datasource.local.PlaylistLocalDataSource
import com.krykun.data.datasource.local.impl.MoviesLocalDataSourceImpl
import com.krykun.data.datasource.local.impl.PlaylistLocalDataSourceImpl
import com.krykun.data.datasource.remote.MoviesRemoteDataSource
import com.krykun.data.datasource.remote.impl.MoviesRemoteDataSourceImpl
import com.krykun.data.repo.MoviesLocalRepoImpl
import com.krykun.data.repo.MoviesRemoteRepoImpl
import com.krykun.data.repo.PlaylistLocalRepoImpl
import com.krykun.domain.repositories.local.MoviesLocalRepo
import com.krykun.domain.repositories.local.PlaylistsLocalRepo
import com.krykun.domain.repositories.remote.MoviesRemoteRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideRemoteDataSource(apiService: ApiService): MoviesRemoteDataSource =
        MoviesRemoteDataSourceImpl(apiService)

    @Provides
    @Singleton
    fun provideMoviesLocalDataSource(movieDao: MovieDao): MoviesLocalDataSource =
        MoviesLocalDataSourceImpl(movieDao)

    @Provides
    @Singleton
    fun providePlaylistsLocalDataSource(playlistDao: PlaylistDao): PlaylistLocalDataSource =
        PlaylistLocalDataSourceImpl(playlistDao)

    @Provides
    @Singleton
    fun provideMoviesRemoteRepo(remoteDataSource: MoviesRemoteDataSource): MoviesRemoteRepo =
        MoviesRemoteRepoImpl(remoteDataSource)

    @Provides
    @Singleton
    fun provideMoviesLocalRepo(moviesLocalDataSource: MoviesLocalDataSource): MoviesLocalRepo =
        MoviesLocalRepoImpl(moviesLocalDataSource)

    @Provides
    @Singleton
    fun providePlaylistsLocalRepo(playlistLocalDataSource: PlaylistLocalDataSource): PlaylistsLocalRepo =
        PlaylistLocalRepoImpl(playlistLocalDataSource)
}