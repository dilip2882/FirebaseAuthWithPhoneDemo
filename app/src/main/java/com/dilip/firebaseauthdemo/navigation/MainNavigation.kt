package com.dilip.firebaseauthdemo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dilip.firebaseauthdemo.features.screens.AuthViewModel
import com.dilip.firebaseauthdemo.ui.NumberScreen
import com.dilip.firebaseauthdemo.ui.MainScreen
import com.dilip.firebaseauthdemo.ui.OtpScreen

@Composable
fun MainNavigation(
    viewModel: AuthViewModel = hiltViewModel()
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Route.NumberScreen.route) {
        composable(Route.NumberScreen.route) {
            NumberScreen(navController, viewModel)
        }
        composable(
            route = Route.OtpScreen.route + "/{${Route.OtpScreen.OTP_ARG}}",
            arguments = listOf(navArgument(Route.OtpScreen.OTP_ARG) { type = NavType.StringType })
        ) { backStackEntry ->
            val otp = backStackEntry.arguments?.getString(Route.OtpScreen.OTP_ARG) ?: ""
            OtpScreen(otp, viewModel)
        }
    }
}
