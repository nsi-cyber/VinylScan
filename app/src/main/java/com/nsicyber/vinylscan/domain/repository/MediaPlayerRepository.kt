package com.nsicyber.vinylscan.domain.repository


interface MediaPlayerRepository {

    fun play()

    fun stop()

    fun pause()

    fun dispose()

    fun resume()

    fun onInfo(onStart: () -> Unit, onFinish: () -> Unit)
    fun onFinish(onFinish: () -> Unit)

    fun setUrl(uri: String?)

}