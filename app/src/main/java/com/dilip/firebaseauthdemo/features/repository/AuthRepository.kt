package com.dilip.firebaseauthdemo.features.repository

import android.app.Activity
import com.dilip.firebaseauthdemo.features.utils.ResultState
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    fun createUserWithPhone(
        phone:String,
        activity:Activity
    ) : Flow<ResultState<String>>

    fun signWithCredential(
        otp:String
    ): Flow<ResultState<String>>

}