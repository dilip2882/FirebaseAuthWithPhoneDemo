package com.dilip.firebaseauthdemo.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.navigation.NavController
import com.dilip.firebaseauthdemo.navigation.Route

private const val TAG = "MainScreen"

@Composable
fun NumberScreen(
    navController: NavController,
) {
    val context = LocalContext.current
    var phoneNumber by remember { mutableStateOf("") }
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

        Text(text = "We will send you OTP", color = tcolor)
        Text(text = "on your phone number", color = tcolor)

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = {
                if (it.length <= 10) phoneNumber = it
            },
            label = {
                Text(text = "Enter Phone Number", color = tcolor)
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = bgColor,
                unfocusedContainerColor = bgColor,
                focusedIndicatorColor = tcolor,
                cursorColor = tcolor,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            textStyle = TextStyle(
                color = tcolor
            )
        )
        Spacer(modifier = Modifier.height(10.dp))

        Button(
            modifier = Modifier.clickable {
                navController.navigate(Route.OtpScreen)
            },
            onClick = {
                onLoginClick(
                    context = context,
                    phoneNumber = phoneNumber,
                    onCodeSend = {},
                    navController = navController
                )
            },
            colors = ButtonDefaults.buttonColors(
                if (phoneNumber.length >= 10) tcolor else Color.Gray
            )
        ) {
            Text(text = "Send OTP", color = Color.White)
        }

    }
}
