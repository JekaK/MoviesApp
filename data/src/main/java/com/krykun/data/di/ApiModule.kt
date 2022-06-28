package com.krykun.data.di

import com.krykun.data.api.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    fun provideLosesService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)
}