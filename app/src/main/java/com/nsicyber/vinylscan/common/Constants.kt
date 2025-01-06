package com.nsicyber.vinylscan.common

object Constants {
    object Destination {
        const val CAMERA_SCREEN = "camera_screen"
        const val DETAIL_SCREEN = "detail_screen"
        const val SEARCH_SCREEN = "search_screen"
    }


    object Endpoints {
        object Discogs {
            const val SEARCH = "/database/search"
            const val GET_DETAIL = "/masters/{masterId}"
        }

        object Deezer {
            const val SEARCH = "/search"
            const val GET_ALBUM_DETAIL = "/album/{albumId}"

        }
    }

}