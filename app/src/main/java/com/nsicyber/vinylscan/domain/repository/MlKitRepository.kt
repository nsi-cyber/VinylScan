package com.nsicyber.vinylscan.domain.repository

import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.flow.Flow

interface MlKitRepository {
    fun recognizeBarcode(inputImage: InputImage): Flow<Result<String?>>
}
