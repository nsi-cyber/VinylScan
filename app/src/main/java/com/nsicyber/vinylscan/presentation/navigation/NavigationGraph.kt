package com.nsicyber.vinylscan.presentation.navigation

import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nsicyber.vinylscan.common.Constants
import com.nsicyber.vinylscan.domain.model.VinylModel
import com.nsicyber.vinylscan.presentation.cameraScreen.CameraEvent
import com.nsicyber.vinylscan.presentation.cameraScreen.CameraScreen
import com.nsicyber.vinylscan.presentation.cameraScreen.CameraViewModel
import com.nsicyber.vinylscan.presentation.detailScreen.DetailScreen
import com.nsicyber.vinylscan.presentation.favoriteScreen.FavoriteScreen
import com.nsicyber.vinylscan.presentation.searchScreen.SearchScreen

@Composable
fun NavigationGraph(
    applicationContext: Context,
    modifier: Modifier = Modifier,
    cameraViewModel: CameraViewModel = hiltViewModel(),

    navController: NavHostController = rememberNavController(),
    startDestination: String = Constants.Destination.CAMERA_SCREEN,
    navActions: NavigationActions = remember(navController) {
        NavigationActions(navController)
    },

    ) {


    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination?.route

    val vinylModel = remember { mutableStateOf<VinylModel?>(null) }

    Scaffold { innerPadding ->


        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = modifier.padding(innerPadding),
        ) {


            composable(route = Constants.Destination.CAMERA_SCREEN) {
                CameraScreen(
                    cameraViewModel = cameraViewModel,
                    applicationContext = applicationContext,
                    navigateToDetail = {
                        vinylModel.value = it
                        navActions.navigateToDetailScreen()
                    },
                    navigateToSearch = {
                        navActions.navigateToSearchScreen()
                    }, navigateToFavorites = {
                        navActions.navigateToFavoritesScreen()
                    }
                )
            }

            composable(
                route = Constants.Destination.DETAIL_SCREEN,
            ) {

                DetailScreen(
                    cameraViewModel = cameraViewModel,
                    onBackPressed = {
                        cameraViewModel.onEvent(CameraEvent.SetStateEmpty)
                        navActions.popBackStack()
                    },
                    data = vinylModel.value
                )
            }


            composable(route = Constants.Destination.SEARCH_SCREEN) {

                SearchScreen(
                    onBackPressed = {
                        cameraViewModel.onEvent(CameraEvent.SetStateEmpty)
                        navActions.popBackStack()
                    },
                    onDetail = {
                        vinylModel.value = it
                        navActions.navigateToDetailScreen()
                    }
                )

            }

            composable(route = Constants.Destination.FAVORITES_SCREEN) {

                FavoriteScreen(
                    onBackPressed = {
                        cameraViewModel.onEvent(CameraEvent.SetStateEmpty)
                        navActions.popBackStack()
                    },
                    onDetail = {
                        vinylModel.value = it
                        navActions.navigateToDetailScreen()
                    }
                )

            }


        }


    }

}
