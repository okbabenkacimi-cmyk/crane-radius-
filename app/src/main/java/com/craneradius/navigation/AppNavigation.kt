package com.craneradius.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.craneradius.presentation.assessment.AssessmentScreen
import com.craneradius.presentation.home.HomeScreen

object Routes {
    const val HOME = "home"
    const val ASSESSMENT = "assessment"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {
        composable(Routes.HOME) {
            HomeScreen(
                onNewAssessment = {
                    navController.navigate(Routes.ASSESSMENT)
                }
            )
        }
        composable(Routes.ASSESSMENT) {
            AssessmentScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
