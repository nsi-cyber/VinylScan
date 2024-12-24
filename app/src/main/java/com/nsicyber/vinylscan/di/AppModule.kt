package com.nsicyber.vinylscan.di

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.nsicyber.vinylscan.data.repository.MlKitRepositoryImpl
import com.nsicyber.vinylscan.domain.repository.MlKitRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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


}
