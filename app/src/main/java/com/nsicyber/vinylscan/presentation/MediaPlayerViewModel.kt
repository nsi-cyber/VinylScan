package com.nsicyber.vinylscan.presentation

import androidx.lifecycle.ViewModel
import com.nsicyber.vinylscan.domain.repository.MediaPlayerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class MediaPlayerViewModel @Inject constructor(private var mediaPlayerRepository: MediaPlayerRepository) :
    ViewModel() {

    fun startMediaPlayer(uri: String?) {
        try {
            mediaPlayerRepository.setUrl(uri)
            mediaPlayerRepository.play()
        } catch (e: Exception) {
        }
    }

    fun resumeMediaPlayer() {
        mediaPlayerRepository.resume()
    }

    fun stopMediaPlayer() {
        mediaPlayerRepository.stop()
        mediaPlayerRepository.dispose()
    }

    fun onFinish(onFinish: () -> Unit) {
        mediaPlayerRepository.onFinish { onFinish() }
    }

    fun pauseMediaPlayer() = mediaPlayerRepository.pause()

}