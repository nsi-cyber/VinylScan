package com.nsicyber.vinylscan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.nsicyber.vinylscan.presentation.navigation.NavigationGraph
import com.nsicyber.vinylscan.ui.theme.VinylScanTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VinylScanTheme {
                NavigationGraph(applicationContext = this)
            }
        }
    }
}

