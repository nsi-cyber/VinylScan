package com.nsicyber.vinylscan.presentation.navigation

import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nsicyber.vinylscan.common.Constants
import com.nsicyber.vinylscan.presentation.cameraScreen.CameraScreen

@Composable
fun NavigationGraph(
    applicationContext: Context,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Constants.Destination.CAMERA_SCREEN,
    navActions: NavigationActions = remember(navController) {
        NavigationActions(navController)
    },

    ) {


    val isMenuShow = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination?.route




    Scaffold { innerPadding ->


        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = modifier.padding(innerPadding),
        ) {


            composable(route = Constants.Destination.CAMERA_SCREEN) {
                CameraScreen(
                    applicationContext = applicationContext,
                )
            }


        }


    }

}



