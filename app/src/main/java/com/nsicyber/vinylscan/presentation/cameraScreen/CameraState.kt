package com.nsicyber.vinylscan.presentation.cameraScreen

import com.nsicyber.vinylscan.domain.model.AlbumModel


data class CameraState(
    val barcode:String="",
    val onSuccess:Boolean=false,
    val isPageLoading:Boolean=false,
    val albumDetail:AlbumModel?=null,

)