package com.dilip.firebaseauthdemo.features.auth

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dilip.firebaseauthdemo.services.auth.AuthService
import com.dilip.firebaseauthdemo.features.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authService: AuthService
) : ViewModel() {

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> get() = _isLoggedIn

    init {
        viewModelScope.launch {
            _isLoggedIn.value = authService.isLoggedIn()
        }
    }

    fun createUserWithPhone(
        mobile: String,
        activity: Activity
    ): Flow<ResultState<String>> = flow {
        authService.createUserWithPhone(mobile, activity).collect { result ->
            emit(result)
        }
    }

    fun signInWithCredential(
        otp: String
    ): Flow<ResultState<String>> = flow {
        authService.signInWithCredential(otp).collect { result ->
            emit(result)
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authService.signOut()
            _isLoggedIn.value = false
        }
    }

    fun onLoggedIn() {
        viewModelScope.launch {
            authService.onLoggedIn()
            _isLoggedIn.value = true
        }
    }
}
