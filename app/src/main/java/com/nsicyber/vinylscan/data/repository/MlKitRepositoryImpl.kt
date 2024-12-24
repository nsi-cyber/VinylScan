package com.nsicyber.vinylscan.data.repository

import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.common.InputImage
import com.nsicyber.vinylscan.domain.repository.MlKitRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await


class MlKitRepositoryImpl(
    private val barcodeScanner: BarcodeScanner
) : MlKitRepository {

    override fun recognizeBarcode(inputImage: InputImage): Flow<Result<String?>> = flow {
        try {
            val emojiList = barcodeScanner.process(inputImage).await()
            val face = emojiList.firstOrNull()
            emit(Result.success(face?.rawValue))

        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)


}
