package com.example.hutbe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.hutbe.ui.theme.HutbeTheme
import com.example.hutbe.view.screens.HutbeApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HutbeTheme {
                HutbeApp()
            }
        }
    }
}