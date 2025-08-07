package com.example.hutbe.controller


import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class MediaPlayerViewModel : ViewModel() {
    private var mediaPlayer: MediaPlayer? = null

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    private val _isPrepared = MutableStateFlow(false)
    val isPrepared: StateFlow<Boolean> = _isPrepared

    private val _currentPosition = MutableStateFlow(0)
    val currentPosition: StateFlow<Int> = _currentPosition

    private val _duration = MutableStateFlow(0)
    val duration: StateFlow<Int> = _duration



    fun playSound(soundUrl: String?) {
        if (isPrepared.value) {
            playPreparedSound()
        } else {
            prepareSound(soundUrl)
        }
    }
    fun prepareSound(soundUrl: String?) {
        mediaPlayer = MediaPlayer().apply {
            setDataSource(soundUrl)
            setOnPreparedListener {
                _duration.value = it.duration
                _isPrepared.value=true
            }
            prepareAsync()
        }
    }

    fun playPreparedSound() {
        if (_isPlaying.value) {
            pause()
        } else {
            mediaPlayer?.start()
            _isPlaying.value = true
            startProgressUpdate()
        }
    }


    private fun startProgressUpdate() {
        viewModelScope.launch {
            while (_isPlaying.value) {
                mediaPlayer?.let {
                    _currentPosition.value = it.currentPosition
                }
                delay(500)
            }
        }
    }

    fun pause() {
        mediaPlayer?.pause()
        _isPlaying.value = false
    }

    fun seekTo(position: Int) {
        mediaPlayer?.seekTo(position)
        _currentPosition.value = position
    }

    fun releaseMediaPlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
        _isPlaying.value = false
        _currentPosition.value = 0
        _duration.value = 0
        _isPrepared.value = false
    }

    override fun onCleared() {
        super.onCleared()
        releaseMediaPlayer()
    }
}
