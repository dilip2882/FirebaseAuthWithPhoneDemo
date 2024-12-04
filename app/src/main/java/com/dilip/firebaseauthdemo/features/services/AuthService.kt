package com.dilip.firebaseauthdemo.features.services

import android.app.Activity
import com.dilip.firebaseauthdemo.features.utils.ResultState
import kotlinx.coroutines.flow.Flow

interface AuthService {

    val currentUserId: String
    val isAuthenticated: Boolean

    fun createUserWithPhone(
        phone: String,
        activity: Activity
    ): Flow<ResultState<String>>

    fun signWithCredential(
        otp: String
    ): Flow<ResultState<String>>

    suspend fun signOut()
}


data class User(
    val id: String = "",
    val isAnonymous: Boolean = true
)