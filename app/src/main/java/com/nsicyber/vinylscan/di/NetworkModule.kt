package com.nsicyber.vinylscan.di

import com.nsicyber.vinylscan.data.remote.DeezerApiService
import com.nsicyber.vinylscan.data.remote.DiscogsApiService
import com.nsicyber.vinylscan.data.repository.DeezerNetworkRepositoryImpl
import com.nsicyber.vinylscan.data.repository.DiscogsNetworkRepositoryImpl
import com.nsicyber.vinylscan.domain.repository.DeezerNetworkRepository
import com.nsicyber.vinylscan.domain.repository.DiscogsNetworkRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(
            HttpLoggingInterceptor().apply {
                level = (HttpLoggingInterceptor.Level.BODY)
            },
        )
            .addInterceptor(
                Interceptor { chain ->
                    val request =
                        chain.request().newBuilder()
                            .build()
                    chain.proceed(request)
                },
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideDiscogsApiService(okHttpClient: OkHttpClient): DiscogsApiService =
        Retrofit.Builder().baseUrl("https://api.discogs.com")
            .addConverterFactory(GsonConverterFactory.create()).client(okHttpClient).build()
            .create(DiscogsApiService::class.java)


    @Provides
    @Singleton
    fun provideNetworkRepository(discogsApiService: DiscogsApiService): DiscogsNetworkRepository {
        return DiscogsNetworkRepositoryImpl(discogsApiService)
    }

    @Provides
    @Singleton
    fun provideDeezerApiService(okHttpClient: OkHttpClient): DeezerApiService =
        Retrofit.Builder().baseUrl("https://api.deezer.com")
            .addConverterFactory(GsonConverterFactory.create()).client(okHttpClient).build()
            .create(DeezerApiService::class.java)


    @Provides
    @Singleton
    fun provideDeezerNetworkRepository(deezerApiService: DeezerApiService): DeezerNetworkRepository {
        return DeezerNetworkRepositoryImpl(deezerApiService)
    }


}

