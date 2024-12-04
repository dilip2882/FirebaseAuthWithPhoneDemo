package com.dilip.firebaseauthdemo.services.auth

import android.app.Activity
import com.dilip.firebaseauthdemo.features.utils.ResultState
import kotlinx.coroutines.flow.Flow

interface AuthService {

    val isAuthenticated: Boolean

    suspend fun createUserWithPhone(
        phone: String,
        activity: Activity
    ): Flow<ResultState<String>>

    suspend fun signInWithCredential(
        otp: String
    ): Flow<ResultState<String>>

    suspend fun signOut()

    suspend fun onLoggedIn()
    suspend fun isLoggedIn(): Boolean
}


data class User(
    val id: String = "",
    val isAnonymous: Boolean = true
)