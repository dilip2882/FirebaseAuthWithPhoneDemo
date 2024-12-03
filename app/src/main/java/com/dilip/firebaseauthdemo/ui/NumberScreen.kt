package com.dilip.firebaseauthdemo.ui

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.dilip.firebaseauthdemo.features.screens.AuthViewModel
import com.dilip.firebaseauthdemo.features.utils.CommonDialog
import com.dilip.firebaseauthdemo.features.utils.ResultState
import com.dilip.firebaseauthdemo.navigation.Route
import com.hbb20.CountryCodePicker
import kotlinx.coroutines.launch
import kotlin.math.log

private const val TAG = "MainScreen"

@Composable
fun NumberScreen(
    navController: NavController,
    viewModel: AuthViewModel
) {
    val context = LocalContext.current
    var phoneNumber by remember { mutableStateOf("") }
    var countryCode by remember { mutableStateOf("+91") }
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
            text = "OTP Verification",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(10.dp))

        Text(text = "We will send you OTP", color = tcolor)
        Text(text = "on your phone number", color = tcolor)

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Country Code Picker
            AndroidView(
                factory = { context ->
                    CountryCodePicker(context).apply {
                        setDefaultCountryUsingPhoneCode(91) // Default
                        setOnCountryChangeListener {
                            countryCode = selectedCountryCodeWithPlus
                        }
                    }
                },
                modifier = Modifier.wrapContentSize()
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Phone number input field
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = {
                    if (it.length <= 10) phoneNumber = it
                },
                label = { Text(text = "Enter Phone Number", color = tcolor) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = bgColor,
                    unfocusedContainerColor = bgColor,
                    focusedIndicatorColor = tcolor,
                    cursorColor = tcolor,
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(0.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                textStyle = TextStyle(
                    color = tcolor
                )
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                val fullNumber = "$countryCode$phoneNumber" // country code with phone number
                isDialog = true

                scope.launch {
                    viewModel.createUserWithPhone(fullNumber, context as Activity).collect {
                        when (it) {
                            is ResultState.Success -> {
                                isDialog = false
                                val otp = it.data
                                navController.navigate(Route.OtpScreen.withOtp(otp))
                            }
                            is ResultState.Failure -> {
                                isDialog = false
                                Toast.makeText(context, it.msg.toString(), Toast.LENGTH_SHORT).show()
                                Log.d(TAG, "NumberScreen: ${it.msg}")
                            }
                            ResultState.Loading -> {
                                isDialog = true
                            }
                        }
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                if (phoneNumber.length == 10) tcolor else Color.Gray
            ),
            enabled = phoneNumber.length == 10
        ) {
            Text(text = "Generate OTP", color = Color.White)
        }
    }
}
