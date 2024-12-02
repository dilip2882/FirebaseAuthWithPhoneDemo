package com.dilip.firebaseauthdemo.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController as NavController1

@Composable
fun OtpScreen(
    onVerificationSuccess: () -> Unit,
    navController: NavController1
) {
    val context = LocalContext.current
    var otp by remember { mutableStateOf("") }
    val bgColor = Color(0xFFECFADC)
    val tcolor = Color(0xFF2b472b)

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

        Text(text = "You will get a OTP via SMS", color = tcolor)

        Spacer(modifier = Modifier.height(10.dp))

        BasicTextField(
            value = otp,
            onValueChange = {
                if (it.length <= 6) otp = it
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
                        index >= otp.length -> ""
                        else -> otp[index]
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
                        ) {}
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))

        Button(
            modifier = Modifier.clickable { onVerificationSuccess() },
            onClick = {
                verifyPhoneNumberWithCode(context, storedVerificationId, otp, navController)
            },
            colors = ButtonDefaults.buttonColors(
                if (otp.length >= 6) tcolor else Color.Gray
            )
        ) {
            Text(text = "Verify", color = Color.White)
        }

    }
}
