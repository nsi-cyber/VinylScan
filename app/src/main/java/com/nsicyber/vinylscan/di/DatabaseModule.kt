package com.nsicyber.vinylscan.di


import android.content.Context
import androidx.room.Room
import com.nsicyber.vinylscan.data.database.dao.AppDatabase
 import com.nsicyber.vinylscan.data.database.dao.VinylDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideCocktailDatabase(
        @ApplicationContext appContext: Context,
    ): AppDatabase =
        Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "database",
        ).build()

    @Provides
    fun provideCocktailDao(database: AppDatabase): VinylDao = database.databaseDao()

}
