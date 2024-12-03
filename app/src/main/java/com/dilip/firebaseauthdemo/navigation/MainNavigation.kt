package com.dilip.firebaseauthdemo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dilip.firebaseauthdemo.ui.NumberScreen
import com.dilip.firebaseauthdemo.ui.MainScreen
import com.dilip.firebaseauthdemo.ui.OtpScreen

@Composable
fun MainNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.HomeScreen
    ) {
        composable<Route.HomeScreen> {
            NumberScreen(
            )
        }

        composable<Route.OtpScreen> {
            OtpScreen(

            )
        }

        composable<Route.MainScreen> {
            MainScreen()
        }

    }

}