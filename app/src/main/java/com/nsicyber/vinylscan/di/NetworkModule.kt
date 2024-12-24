package com.nsicyber.vinylscan.di

import com.google.mlkit.vision.barcode.BarcodeScanner
import com.nsicyber.vinylscan.data.remote.ApiService
import com.nsicyber.vinylscan.data.repository.MlKitRepositoryImpl
import com.nsicyber.vinylscan.data.repository.NetworkRepositoryImpl
import com.nsicyber.vinylscan.domain.repository.MlKitRepository
import com.nsicyber.vinylscan.domain.repository.NetworkRepository
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
    fun provideApiService(okHttpClient: OkHttpClient): ApiService =
        Retrofit.Builder().baseUrl("https://api.discogs.com")
            .addConverterFactory(GsonConverterFactory.create()).client(okHttpClient).build()
            .create(ApiService::class.java)


    @Provides
    @Singleton
    fun provideNetworkRepository(apiService: ApiService): NetworkRepository {
        return NetworkRepositoryImpl(apiService)
    }


}

