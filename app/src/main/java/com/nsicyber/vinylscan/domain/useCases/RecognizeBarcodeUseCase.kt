package com.nsicyber.vinylscan.domain.useCases

import com.google.mlkit.vision.common.InputImage
import com.nsicyber.vinylscan.domain.repository.MlKitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class RecognizeBarcodeUseCase @Inject constructor(
    private val repository: MlKitRepository
) {
    operator fun invoke(bitmap: InputImage): Flow<Result<String?>> =
        flow {
            try {
                repository.recognizeBarcode(bitmap)
                    .collect { result1 ->
                        emit(result1)
                    }
            } catch (e: Exception) {
                emit(Result.failure(e))
            }
        }

}
