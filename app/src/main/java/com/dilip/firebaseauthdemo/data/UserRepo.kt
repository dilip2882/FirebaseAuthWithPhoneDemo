package com.dilip.firebaseauthdemo.data

import com.dilip.firebaseauthdemo.services.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class UserRepo {

    suspend fun saveUser(user: User) {
        Firebase.firestore
            .collection("users")
            .add(user)
            .await()
    }
}
