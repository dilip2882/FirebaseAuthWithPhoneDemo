package com.dilip.firebaseauthdemo.navigation

sealed class Route(val route: String) {
    object NumberScreen : Route("number_screen")
    object OtpScreen : Route("otp_screen") {
        const val OTP_ARG = "otp"
        const val PHONE_ARG = "phone"

        fun withOtpAndPhone(otp: String, phone: String): String {
            return "$route/$otp/$phone"
        }
    }

    object HomeScreen : Route("home_screen")
}
