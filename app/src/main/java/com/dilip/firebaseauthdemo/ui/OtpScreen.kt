package com.dilip.firebaseauthdemo.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dilip.firebaseauthdemo.features.screens.AuthViewModel
import com.dilip.firebaseauthdemo.features.utils.CommonDialog
import com.dilip.firebaseauthdemo.features.utils.ResultState
import kotlinx.coroutines.launch

@Composable
fun OtpScreen(
    otp: String,
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
            .background(bgColor),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Verification",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(10.dp))

        Text(text = "Enter OTP sent to your phone", color = tcolor)

        Spacer(modifier = Modifier.height(10.dp))

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
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                repeat(6) { index ->
                    val number = when {
                        index >= otpInput.length -> ""
                        else -> otpInput[index]
                    }

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = number.toString(),
                            style = MaterialTheme.typography.titleLarge,
                            color = tcolor,
                        )

                        Box(
                            modifier = Modifier
                                .width(40.dp)
                                .height(2.dp)
                                .background(tcolor)
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))

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
                            }

                            is ResultState.Failure -> {
                                isDialog = false
                                Toast.makeText(context, it.msg.toString(), Toast.LENGTH_SHORT)
                                    .show()
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
            enabled = otpInput.length == 6
        ) {
            Text(text = "Verify OTP", color = Color.White)
        }
    }
}
