package com.nsicyber.vinylscan.presentation

import androidx.lifecycle.ViewModel
import com.nsicyber.vinylscan.domain.repository.MediaPlayerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class MediaPlayerViewModel @Inject constructor(private var mediaPlayerRepository: MediaPlayerRepository) :
    ViewModel() {
    var isBuffering = true

    fun startMediaPlayer(uri: String?) {
        try {
            isBuffering = true

            onBufferListener()
            mediaPlayerRepository.setUrl(uri)
            mediaPlayerRepository.play()
        } catch (e: Exception) {
        }
    }


    fun onBufferListener() {
        mediaPlayerRepository.onInfo(
            onFinish = {
                isBuffering = false
                       },
            onStart = {
                isBuffering = true
            })
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