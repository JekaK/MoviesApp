package com.krykun.data.di

import android.content.Context
import com.krykun.data.R
import com.krykun.data.ext.hasNetwork
import com.krykun.data.interceptor.ConnectivityInterceptor
import com.krykun.data.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideBaseUrl(): String = Constants.BASE_URL

    @Provides
    @Singleton
    fun provideCache(@ApplicationContext context: Context) =
        Cache(context.cacheDir, (5 * 1024 * 1024).toLong())

    @Singleton
    @Provides
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
        cache: Cache
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor { chain ->
                var request = chain.request()

                request = if (context.hasNetwork() == true) {
                    request.newBuilder().header(
                        "Cache-Control",
                        "public, max-age=" + 30
                    ).build()
                } else {
                    request.newBuilder().header(
                        "Cache-Control",
                        "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7
                    ).build()
                }

                chain.proceed(request)
            }
            .addInterceptor(loggingInterceptor)
            .addInterceptor(ConnectivityInterceptor(context))
            .addInterceptor(Interceptor { chain ->
                val original: Request = chain.request()
                val originalHttpUrl: HttpUrl = original.url

                val url = originalHttpUrl.newBuilder()
                    .addQueryParameter("api_key", context.resources.getString(R.string.API_KEY))
                    .build()

                val requestBuilder = original.newBuilder()
                    .url(url)

                val request = requestBuilder.build()
                chain.proceed(request)
            })
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .connectionPool(ConnectionPool(0, 5, TimeUnit.MINUTES))
            .protocols(listOf(Protocol.HTTP_1_1))
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, BASE_URL: String): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()
}