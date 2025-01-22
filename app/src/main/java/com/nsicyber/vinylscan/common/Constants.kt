package com.nsicyber.vinylscan.common

object Constants {
    object Destination {
        const val CAMERA_SCREEN = "camera_screen"
        const val DETAIL_SCREEN = "detail_screen"
        const val SEARCH_SCREEN = "search_screen"
        const val FAVORITES_SCREEN = "favorites_screen"
    }


    object Endpoints {
        object Discogs {
            const val SEARCH = "/database/search"
            const val GET_MASTER_DETAIL = "/masters/{masterId}"
            const val GET_RELEASE_DETAIL = "/releases/{releaseId}"
        }

        object Deezer {
            const val SEARCH = "/search"
            const val GET_ALBUM_DETAIL = "/album/{albumId}"

        }
    }

}