package com.example.hutbe.view.screens

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hutbe.controller.HutbeViewModel
import com.example.hutbe.controller.MediaPlayerViewModel
import com.example.hutbe.model.Hutbe
import com.google.gson.Gson

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
                navController = navController,
                hutbeViewModel = hutbeViewModel,
            )
        }
        composable("hutbeDetail/{hutbeJson}") { backStackEntry ->
            val hutbeJson = backStackEntry.arguments?.getString("hutbeJson")
            val hutbe = Gson().fromJson(hutbeJson, Hutbe::class.java)

            HutbeDetailScreen(
                hutbe = hutbe,
                mediaPlayerViewModel = mediaPlayerViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

    }
}
