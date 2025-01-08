package com.nsicyber.vinylscan.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import com.nsicyber.vinylscan.common.Constants


class NavigationActions(private val navController: NavHostController) {


    fun navigateToCameraScreen() {
        navController.navigate(Constants.Destination.CAMERA_SCREEN) {
            popUpToTop(navController)
        }
    }

    fun navigateToSearchScreen() {
        navController.navigate(Constants.Destination.SEARCH_SCREEN) {
            popUpToTop(navController)
        }
    }

    fun navigateToDetailScreen() {
        navController.navigate(Constants.Destination.DETAIL_SCREEN) {
            popUpToTop(navController)
        }
    }


    fun popBackStack() {
        navController.popBackStack()
    }


}

fun NavOptionsBuilder.popUpToTop(navController: NavController, clean: Boolean = false) {
    popUpTo(navController.currentBackStackEntry?.destination?.route ?: return) {
        inclusive = clean
    }
}
