package com.nsicyber.vinylscan.data.repository

import android.media.AudioAttributes
import android.media.MediaPlayer
import com.nsicyber.vinylscan.domain.repository.MediaPlayerRepository
import javax.inject.Inject


class MediaPlayerRepositoryImpl @Inject constructor() : MediaPlayerRepository {

    var mediaPlayer = MediaPlayer()


    override fun play() {
        mediaPlayer.prepare()
        AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()
        mediaPlayer.start()
    }

    override fun resume() {
        mediaPlayer.start()
    }

    override fun onInfo(onStart: () -> Unit, onFinish: () -> Unit) {
        mediaPlayer.setOnPreparedListener {
            onFinish()
        }

    }

    override fun onFinish(onFinish: () -> Unit) {
        mediaPlayer.setOnCompletionListener {
            onFinish()
        }
    }


    override fun stop() {
        mediaPlayer.reset()
        mediaPlayer.stop()
    }

    override fun pause() {
        mediaPlayer.pause()
    }

    override fun dispose() {
        mediaPlayer.reset()
        mediaPlayer.release()
        mediaPlayer = MediaPlayer()
    }

    override fun setUrl(uri: String?) {
        if (uri == null) return;
        mediaPlayer.setDataSource(uri);
    }

}