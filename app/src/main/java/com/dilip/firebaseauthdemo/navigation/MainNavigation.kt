package com.dilip.firebaseauthdemo.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dilip.firebaseauthdemo.features.auth.AuthViewModel
import com.dilip.firebaseauthdemo.features.home.HomeScreen
import com.dilip.firebaseauthdemo.features.auth.NumberScreen
import com.dilip.firebaseauthdemo.features.auth.OtpScreen

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
            route = Route.OtpScreen.route + "/{${Route.OtpScreen.OTP_ARG}}/{${Route.OtpScreen.PHONE_ARG}}",
            arguments = listOf(
                navArgument(Route.OtpScreen.OTP_ARG) { type = NavType.StringType },
                navArgument(Route.OtpScreen.PHONE_ARG) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val otp = backStackEntry.arguments?.getString(Route.OtpScreen.OTP_ARG) ?: ""
            val phoneNumber = backStackEntry.arguments?.getString(Route.OtpScreen.PHONE_ARG) ?: ""
            OtpScreen(otp, phoneNumber, navController, viewModel)
        }
        composable(Route.HomeScreen.route) {
            HomeScreen()
        }
    }
}
