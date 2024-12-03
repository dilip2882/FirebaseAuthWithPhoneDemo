package com.dilip.firebaseauthdemo.ui

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.dilip.firebaseauthdemo.R
import com.dilip.firebaseauthdemo.features.screens.AuthViewModel
import com.dilip.firebaseauthdemo.features.utils.CommonDialog
import com.dilip.firebaseauthdemo.features.utils.ResultState
import com.dilip.firebaseauthdemo.navigation.Route
import kotlinx.coroutines.launch

@Composable
fun OtpScreen(
    otp: String,
    phoneNumber: String,
    navController: NavController,
    viewModel: AuthViewModel
) {
    val context = LocalContext.current
    var otpInput by remember { mutableStateOf("") }
    val bgColor = Color(0xFFFFFFFF)
    val tcolor = Color(0xFF2b472b)
    var isDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    if (isDialog) {
        CommonDialog()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Image(
            painter = painterResource(id = R.drawable.mail_box_img),
            contentDescription = null,
            modifier = Modifier.size(150.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "OTP Verification",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = tcolor
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Enter OTP sent to $phoneNumber",
            color = Color.Gray,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        BasicTextField(
            value = otpInput,
            onValueChange = {
                if (it.length <= 6) otpInput = it
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(6) { index ->
                    val char = otpInput.getOrNull(index)?.toString() ?: ""

                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
                            .background(Color(0xFFF5F5F5)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = char,
                            style = MaterialTheme.typography.bodyLarge,
                            color = tcolor
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Don’t receive the OTP?",
            color = Color.Gray,
            fontSize = 14.sp
        )

        Text(
            text = "RESEND OTP",
            color = tcolor,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable {
                isDialog = true

                scope.launch {
                    viewModel.createUserWithPhone(phoneNumber, context as Activity).collect {
                        when (it) {
                            is ResultState.Success -> {
                                isDialog = false
                                Toast.makeText(
                                    context,
                                    "OTP Resent Successfully!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            is ResultState.Failure -> {
                                isDialog = false
                                Toast.makeText(context, "Failed to resend OTP", Toast.LENGTH_SHORT)
                                    .show()
                            }

                            ResultState.Loading -> {
                                isDialog = true
                            }
                        }
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                isDialog = true

                scope.launch {
                    viewModel.signInWithCredential(otpInput).collect {
                        when (it) {
                            is ResultState.Success -> {
                                isDialog = false
                                Toast.makeText(
                                    context,
                                    "OTP verified successfully!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                navController.navigate(Route.HomeScreen.route) {
                                    popUpTo(Route.NumberScreen.route) { inclusive = true }
                                }
                            }

                            is ResultState.Failure -> {
                                isDialog = false
                                Toast.makeText(context, "Wrong OTP", Toast.LENGTH_SHORT).show()
                            }

                            ResultState.Loading -> {
                                isDialog = true
                            }
                        }
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                if (otpInput.length == 6) tcolor else Color.Gray
            ),
            enabled = otpInput.length == 6,
            shape = RoundedCornerShape(50)
        ) {
            Text(text = "VERIFY", color = Color.White)
        }
    }
}
