package com.example.hutbe.view.components


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MediaPlayerControls(
    isPlaying: Boolean,
    duration: Int,
    currentPosition: Int,
    onPlayPause: () -> Unit,
    onSeekChanged: (position: Int) -> Unit
) {
    Column(
        modifier = Modifier
            .navigationBarsPadding().padding(horizontal = 16.dp)
    ) {
        Slider(
            value = currentPosition.toFloat(),
            onValueChange = { onSeekChanged(it.toInt()) },
            valueRange = 0f..duration.toFloat().coerceAtLeast(1f),
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(formatMillis(currentPosition))
            IconButton(onClick = onPlayPause) {
                Icon(
                    if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (isPlaying) "Duraklat" else "Oynat"
                )
            }
            Text(formatMillis(duration))
        }
    }
}

private fun formatMillis(millis: Int): String {
    val seconds = (millis / 1000) % 60
    val minutes = (millis / (1000 * 60)) % 60
    return String.format("%02d:%02d", minutes, seconds)
}