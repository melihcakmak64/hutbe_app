package com.example.hutbe.view.screens

import PdfViewer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.hutbe.controller.HutbeViewModel
import com.example.hutbe.controller.MediaPlayerViewModel
import com.example.hutbe.model.Hutbe
import com.example.hutbe.ui.theme.GreenPrimary
import com.example.hutbe.view.components.MediaPlayerControls

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HutbeDetailScreen(
    hutbe: Hutbe,
    mediaPlayerViewModel: MediaPlayerViewModel,
    onNavigateBack: () -> Unit
) {
    val isPlaying by mediaPlayerViewModel.isPlaying.collectAsState()
    val duration by mediaPlayerViewModel.duration.collectAsState()
    val currentPosition by mediaPlayerViewModel.currentPosition.collectAsState()

    DisposableEffect(Unit) {
            mediaPlayerViewModel.prepareSound(hutbe.Ses)

        onDispose {
            mediaPlayerViewModel.releaseMediaPlayer()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = GreenPrimary,
                    titleContentColor = Color.White
                ),
                title = { Text(hutbe.Title) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Geri",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        bottomBar = {
            MediaPlayerControls(
                isPlaying = isPlaying,
                duration = duration,
                currentPosition = currentPosition,
                onPlayPause = {
                    mediaPlayerViewModel.playSound(hutbe.Ses)
                },
                onSeekChanged = { position ->
                    mediaPlayerViewModel.seekTo(position)
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                "Tarih: ${hutbe.Tarih ?: "Bilinmiyor"}",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            PdfViewer(url = hutbe.PDF!!, modifier = Modifier.fillMaxSize())
        }
    }
}
