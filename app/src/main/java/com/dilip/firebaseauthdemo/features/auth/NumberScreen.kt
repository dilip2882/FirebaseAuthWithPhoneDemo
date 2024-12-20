package com.dilip.firebaseauthdemo.features.auth

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.dilip.firebaseauthdemo.R
import com.dilip.firebaseauthdemo.features.utils.CommonDialog
import com.dilip.firebaseauthdemo.features.utils.ResultState
import com.dilip.firebaseauthdemo.navigation.Route
import com.hbb20.CountryCodePicker
import kotlinx.coroutines.launch

@Composable
fun NumberScreen(
    navController: NavController,
    viewModel: AuthViewModel
) {
    val context = LocalContext.current
    var phoneNumber by remember { mutableStateOf("") }
    var countryCode by remember { mutableStateOf("+91") }
    var isDialogVisible by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Image(
            painter = painterResource(id = R.drawable.mail_box_img),
            contentDescription = null,
            modifier = Modifier.size(150.dp)
        )

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "OTP Verification",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2b472b)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "We will send you a One Time Password",
            color = Color.Gray,
            fontSize = 16.sp
        )

        Text(
            text = "on this mobile number",
            color = Color.Gray,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
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
                modifier = Modifier
                    .width(100.dp)
                    .height(56.dp)
                    .padding(top = 10.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            OutlinedTextField(
                value = phoneNumber,
                onValueChange = {
                    if (it.length <= 10) phoneNumber = it
                },
                label = { Text(text = "Enter Mobile Number", color = Color(0xFF2b472b)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                textStyle = TextStyle(color = Color(0xFF2b472b))
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            shape = RoundedCornerShape(50),
            onClick = {
                val fullNumber = "$countryCode$phoneNumber" // country code with phone number
                isDialogVisible = true

                scope.launch {
                    viewModel.createUserWithPhone(fullNumber, context as Activity).collect {
                        when (it) {
                            is ResultState.Success -> {
                                isDialogVisible = false
                                val otp = it.data
                                navController.navigate(
                                    Route.OtpScreen.withOtpAndPhone(
                                        otp,
                                        fullNumber
                                    )
                                )
                            }

                            is ResultState.Failure -> {
                                isDialogVisible = false
                                Toast.makeText(context, "Failed to send OTP", Toast.LENGTH_SHORT)
                                    .show()
                            }

                            ResultState.Loading -> {
                                isDialogVisible = true
                            }
                        }
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                if (phoneNumber.length == 10) Color(0xFF2b472b) else Color.Gray
            ),
            enabled = phoneNumber.length == 10
        ) {
            Text(text = "Generate OTP", color = Color.White)
        }
    }

    if (isDialogVisible) {
        CommonDialog()
    }
}
