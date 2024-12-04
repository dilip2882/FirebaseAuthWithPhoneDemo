package com.dilip.firebaseauthdemo.services.auth

import android.app.Activity
import com.dilip.firebaseauthdemo.features.utils.DataStoreUtil
import com.dilip.firebaseauthdemo.features.utils.ResultState
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AuthServiceImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val dataStoreUtil: DataStoreUtil
) : AuthService {

    private lateinit var omVerificationCode: String

    override val isAuthenticated: Boolean
        get() = auth.currentUser != null && auth.currentUser?.isAnonymous == false

    override suspend fun createUserWithPhone(
        phone: String,
        activity: Activity
    ): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)

            val onVerificationCallback =
                object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(p0: PhoneAuthCredential) {}

                    override fun onVerificationFailed(p0: FirebaseException) {
                        trySend(ResultState.Failure(p0))
                    }

                    override fun onCodeSent(
                        verificationCode: String,
                        p1: PhoneAuthProvider.ForceResendingToken
                    ) {
                        super.onCodeSent(verificationCode, p1)
                        trySend(ResultState.Success("OTP Sent Successfully"))
                        omVerificationCode = verificationCode
                    }
                }

            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(activity)
                .setCallbacks(onVerificationCallback)
                .build()

            PhoneAuthProvider.verifyPhoneNumber(options)
            awaitClose {
                close()
            }
        }

    override suspend fun signInWithCredential(otp: String): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)

            val code = omVerificationCode

            if (code.isNullOrEmpty()) {
                trySend(ResultState.Failure(IllegalStateException("Verification code not initialized")))

            } else {
                val credential = PhoneAuthProvider.getCredential(code, otp)
                auth.signInWithCredential(credential)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            trySend(ResultState.Success("OTP verified"))
                        } else {
                            trySend(ResultState.Failure(it.exception ?: Exception("Unknown error")))
                        }
                    }
                    .addOnFailureListener {
                        trySend(ResultState.Failure(it))
                    }
            }

            awaitClose {
                close()
            }
        }

    override suspend fun signOut() {
        if (auth.currentUser?.isAnonymous == true) {
            auth.currentUser?.delete()
        }

        auth.signOut()
        dataStoreUtil.setData("isLoggedIn", false) // logged out in DataStore
    }

    override suspend fun onLoggedIn() {
        dataStoreUtil.setData("isLoggedIn", true)
    }

    override suspend fun isLoggedIn(): Boolean {
        return dataStoreUtil.getData("isLoggedIn") ?: false
    }
}
