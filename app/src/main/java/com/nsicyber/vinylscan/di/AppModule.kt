package com.nsicyber.vinylscan.di

import android.content.Context
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.nsicyber.vinylscan.data.database.dao.VinylDao
import com.nsicyber.vinylscan.data.datastore.AppPreferences
import com.nsicyber.vinylscan.data.repository.DatabaseRepositoryImpl
import com.nsicyber.vinylscan.data.repository.MediaPlayerRepositoryImpl
import com.nsicyber.vinylscan.data.repository.MlKitRepositoryImpl
import com.nsicyber.vinylscan.domain.repository.DatabaseRepository
import com.nsicyber.vinylscan.domain.repository.MediaPlayerRepository
import com.nsicyber.vinylscan.domain.repository.MlKitRepository
import com.nsicyber.vinylscan.utils.ReviewManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideBarcodeScanner(): BarcodeScanner {
        return BarcodeScanning.getClient()
    }


    @Provides
    @Singleton
    fun provideMlKitRepository(barcodeScanner: BarcodeScanner): MlKitRepository {
        return MlKitRepositoryImpl(barcodeScanner)
    }


    @Provides
    @Singleton
    fun provideDatabaseRepositoryRepository(dao: VinylDao): DatabaseRepository {
        return DatabaseRepositoryImpl(dao)
    }


    @Provides
    @Singleton
    fun provideMediaPlayerRepository(): MediaPlayerRepository {
        return MediaPlayerRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideAppPreferences(@ApplicationContext context: Context): AppPreferences {
        return AppPreferences(context)
    }

    @Provides
    @Singleton
    fun provideReviewManager(): ReviewManager {
        return ReviewManager()
    }
}
