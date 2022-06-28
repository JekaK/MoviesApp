package com.krykun.data.di

import com.krykun.data.api.ApiService
import com.krykun.data.datasource.MoviesRemoteDataSource
import com.krykun.data.datasource.impl.MoviesRemoteDataSourceImpl
import com.krykun.data.repo.MoviesRemoteRepoImpl
import com.krykun.domain.repositories.MoviesRemoteRepo
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
    fun provideLosesRemoteData(remoteDataSource: MoviesRemoteDataSource): MoviesRemoteRepo =
        MoviesRemoteRepoImpl(remoteDataSource)
}