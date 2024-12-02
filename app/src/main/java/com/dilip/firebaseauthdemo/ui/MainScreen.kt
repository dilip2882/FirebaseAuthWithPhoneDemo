package com.dilip.firebaseauthdemo.ui

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.dilip.firebaseauthdemo.navigation.Route
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

private val TAG = "MainScreen"

@Composable
fun MainScreen() {
    Text(text = "Main Screen!")
}


val auth = FirebaseAuth.getInstance()
var storedVerificationId = ""

fun signInWithPhoneAuthCredential(context: Context, credential: PhoneAuthCredential, navController: NavController) {
    auth.signInWithCredential(credential)
        .addOnCompleteListener(context as Activity) { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
                navController.navigate(route = Route.MainScreen)
            } else {
                if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(context, "Wrong Credentials", Toast.LENGTH_SHORT).show()
                }
            }
        }
}

fun onLoginClick(context: Context, phoneNumber: String, onCodeSend: () -> Unit, navController: NavController) {
    auth.setLanguageCode("en")
    val callback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(p0: PhoneAuthCredential) {
            Log.d(TAG, "onVerificationCompleted: Verification Completed")
            signInWithPhoneAuthCredential(context, p0, navController)
        }

        override fun onVerificationFailed(p0: FirebaseException) {
            Log.d(TAG, "onVerificationFailed: Verification Failed")
        }

        override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
            super.onCodeSent(p0, p1)
            Log.d(TAG, "onCodeSent: Code Sent")
            storedVerificationId = p0
            onCodeSend()
        }
    }

    val options = PhoneAuthOptions.newBuilder(auth)
        .setPhoneNumber("+91$phoneNumber")
        .setTimeout(60L, TimeUnit.SECONDS)
        .setActivity(context as Activity)
        .setCallbacks(callback)
        .build()

    PhoneAuthProvider.verifyPhoneNumber(options)
}

fun verifyPhoneNumberWithCode(context: Context, verificationId: String, code: String, navController: NavController) {
    val credential = PhoneAuthProvider.getCredential(verificationId, code)
    signInWithPhoneAuthCredential(context, credential, navController)
}
