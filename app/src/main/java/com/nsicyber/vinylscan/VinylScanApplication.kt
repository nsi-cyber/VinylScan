package com.nsicyber.vinylscan

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class VinylScanApplication : Application(){
    private val apiKey: String
        get() {
            val remoteConfig = Firebase.remoteConfig
            return remoteConfig.getString("api_key")
        }
    init {
        val remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(mapOf("api_key" to ""))
        fetchRemoteConfig()
    }
    private fun fetchRemoteConfig() {
        Firebase.remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
            }
    }
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)




    }
}