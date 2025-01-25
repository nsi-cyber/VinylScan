package com.nsicyber.vinylscan.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_preferences")

@Singleton
class AppPreferences @Inject constructor(private val context: Context) {

    private object PreferencesKeys {
        val SUCCESSFUL_SCAN_COUNT = intPreferencesKey("successful_scan_count")
    }

    val successfulScanCount: Flow<Int> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.SUCCESSFUL_SCAN_COUNT] ?: 0
        }

    suspend fun incrementSuccessfulScanCount() {
        context.dataStore.edit { preferences ->
            val currentCount = preferences[PreferencesKeys.SUCCESSFUL_SCAN_COUNT] ?: 0
            preferences[PreferencesKeys.SUCCESSFUL_SCAN_COUNT] = currentCount + 1
        }
    }

    suspend fun resetSuccessfulScanCount() {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.SUCCESSFUL_SCAN_COUNT] = 0
        }
    }
} 