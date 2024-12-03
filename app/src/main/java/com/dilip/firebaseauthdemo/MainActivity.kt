package com.dilip.firebaseauthdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.dilip.firebaseauthdemo.features.screens.PhoneAuthScreen
import com.dilip.firebaseauthdemo.navigation.MainNavigation
import com.dilip.firebaseauthdemo.ui.NumberScreen
import com.dilip.firebaseauthdemo.ui.theme.FirebaseAuthDemoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FirebaseAuthDemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {innerPadding ->
                    MainNavigation()
//                    PhoneAuthScreen(activity = this)
//                    NumberScreen()

                }
            }
        }
    }
}

