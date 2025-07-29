package com.example.hutbe.view.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hutbe.controller.HutbeViewModel
import com.example.hutbe.controller.MediaPlayerViewModel

@Composable
fun HutbeApp() {
    val navController = rememberNavController()
    val hutbeViewModel: HutbeViewModel = viewModel()
    val mediaPlayerViewModel: MediaPlayerViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "hutbeList"
    ) {
        composable("hutbeList") {
            HutbeListScreen(
                hutbeViewModel = hutbeViewModel,
                onNavigateToDetail = {
                    navController.navigate("hutbeDetail")
                }
            )
        }
        composable("hutbeDetail") {
            HutbeDetailScreen(
                hutbeViewModel = hutbeViewModel,
                mediaPlayerViewModel = mediaPlayerViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
