package com.example.hutbe.controller


import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hutbe.model.Hutbe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class MediaPlayerViewModel : ViewModel() {
    private var mediaPlayer: MediaPlayer? = null

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    private val _currentPosition = MutableStateFlow(0)
    val currentPosition: StateFlow<Int> = _currentPosition

    private val _duration = MutableStateFlow(0)
    val duration: StateFlow<Int> = _duration

    private val _currentlyPlayingHutbe = MutableStateFlow<Hutbe?>(null)
    val currentlyPlayingHutbe: StateFlow<Hutbe?> = _currentlyPlayingHutbe

    fun playHutbe(hutbe: Hutbe) {
        if (_currentlyPlayingHutbe.value?.ID == hutbe.ID) {
            playPreparedHutbe()
        } else {
            prepareHutbe(hutbe)
        }
    }
    fun prepareHutbe(hutbe: Hutbe) {
        if (_currentlyPlayingHutbe.value?.ID == hutbe.ID) return

        releaseMediaPlayer()

        mediaPlayer = MediaPlayer().apply {
            setDataSource(hutbe.Ses)
            setOnPreparedListener {
                _duration.value = it.duration
                _currentlyPlayingHutbe.value = hutbe
                // Çalmıyoruz, sadece süreyi aldık
            }
            prepareAsync()
        }
    }

    fun playPreparedHutbe() {
        if (_isPlaying.value) {
            pause()
        } else {
            mediaPlayer?.start()
            _isPlaying.value = true
            startProgressUpdate()
        }
    }



    private fun startNewHutbe(hutbe: Hutbe) {
        releaseMediaPlayer()

        mediaPlayer = MediaPlayer().apply {

            setDataSource(hutbe.Ses)
            prepareAsync()
            setOnPreparedListener {
                start()
                _isPlaying.value = true
                _duration.value = it.duration
                _currentlyPlayingHutbe.value = hutbe
                startProgressUpdate()
            }
            setOnCompletionListener {
                _isPlaying.value = false
                _currentPosition.value = 0
            }
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

    fun resume() {
        mediaPlayer?.start()
        _isPlaying.value = true
        startProgressUpdate()
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
        _currentlyPlayingHutbe.value = null
    }

    override fun onCleared() {
        super.onCleared()
        releaseMediaPlayer()
    }
}
