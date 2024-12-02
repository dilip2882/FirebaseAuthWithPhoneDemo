package com.dilip.firebaseauthdemo.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Route {

    @Serializable
    object HomeScreen : Route()

    @Serializable
    object OtpScreen : Route()

    @Serializable
    object MainScreen : Route()
}

