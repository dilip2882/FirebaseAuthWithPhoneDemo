package com.dilip.firebaseauthdemo.navigation


sealed class Route(val route: String) {
    object NumberScreen : Route("number_screen")
    object OtpScreen : Route("otp_screen") {
        const val OTP_ARG = "otp"
        fun withOtp(otp: String) = "otp_screen/$otp"
    }
}
