package com.nsicyber.vinylscan.common

object Constants {

    object Destination {
        const val MAP_SCREEN = "map_screen"
        const val CAMERA_SCREEN = "camera_screen"
    }

    object Firestore {
        const val COLLECTION = "emojis"
    }

    object Endpoints {
        object Discogs {
            const val SEARCH = "/database/search"
            const val GET_DETAIL = "/masters/{masterId}"
        }

        object Deezer {
            const val SEARCH = "/search/album"
            const val GET_ALBUM_DETAIL = "/album/{albumId}"

        }
    }

}