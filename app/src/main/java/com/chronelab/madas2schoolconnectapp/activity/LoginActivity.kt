package com.chronelab.madas2schoolconnectapp.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions

import androidx.compose.material3.*
import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.chronelab.madas2schoolconnectapp.RoomApplication
import com.chronelab.madas2schoolconnectapp.model.SessionManager
import com.chronelab.madas2schoolconnectapp.model.User
import com.chronelab.madas2schoolconnectapp.schoolconnectdatabse.repository.UserRepositoryInterface
import com.chronelab.madas2schoolconnectapp.ui.theme.madas2schoolconnectappTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

import com.chronelab.madas2schoolconnectapp.R

class LoginActivity : ComponentActivity() {
    private lateinit var userRepository: UserRepositoryInterface
    private lateinit var sessionManager: SessionManager

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as? RoomApplication
        if (app != null) {
            userRepository = app.userRepository
        } else {
            Log.e("LoginActivity", "Application context is not RoomApplication.")
            finish()
            return
        }

        sessionManager = SessionManager(this)

        // Checks for existing session
        if (sessionManager.isSessionActive()) {
            val user = sessionManager.getUser()
            if (user != null) {
                Log.d("LoginActivity", "Active session found, navigating to HomeActivity")
                navigateToHome(user)
                return
            }
        }

        setContent {
            madas2schoolconnectappTheme {
                var errorMessage by remember { mutableStateOf<String?>(null) }
                var isLoading by remember { mutableStateOf(false) }

                LoginScreen(
                    onLogin = { email, password ->
                        if (!isValidEmail(email)) {
                            errorMessage = "Invalid email format"
                        } else {
                            isLoading = true
                            errorMessage = null
                            performLogin(email, password) { result ->
                                isLoading = false
                                errorMessage = result
                            }
                        }
                    },
                    onSignupClick = {
                        Log.d("LoginActivity", "Navigate to Registration")
                        startActivity(Intent(this, RegistrationActivity::class.java))
                    },
                    errorMessage = errorMessage,
                    isLoading = isLoading
                )
            }
        }
    }

    private fun performLogin(email: String, password: String, onResult: (String?) -> Unit) {
        lifecycleScope.launch {
            try {
                Log.d("LoginActivity", "Attempting login for email: $email")
                val user = userRepository.getUser(email, password)
                if (user != null) {
                    Log.i("LoginActivity", "Login successful for ${user.email}, role: ${user.role}")
                    sessionManager.startSession(user)

                    withContext(Dispatchers.Main) {
                        navigateToHome(user)
                    }
                } else {
                    Log.w("LoginActivity", "User not found for email: $email")
                    withContext(Dispatchers.Main) {
                        onResult("Incorrect email or password")
                    }
                }
            } catch (e: Exception) {
                Log.e("LoginActivity", "Error during login", e)
                withContext(Dispatchers.Main) {
                    onResult("An error occurred. Please try again.")
                }
            }
        }
    }



    private fun navigateToHome(user: User) {
        Log.d("LoginActivity", "Navigating to HomeActivity for user: ${user.email}")
        val intent = Intent(this, HomeActivity::class.java).apply {
            putExtra("key_user", user)
        }
        startActivity(intent)
        finish()
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LoginScreen(
    onLogin: (String, String) -> Unit,
    onSignupClick: () -> Unit,
    errorMessage: String?,
    isLoading: Boolean
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val topAppBarBackgroundColor = Color(0xFFE91E63) // TopAppBar background color
    val topAppBarTextColor = Color.White // TopAppBar text color

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(topAppBarBackgroundColor)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 32.dp, bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top // Changed to Top for better spacing
        ) {
            //  Logo Image
            Image(
                painter = painterResource(id = R.drawable.schoolconnect_logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(200.dp) // Increased size
                    .padding(bottom = 24.dp) // Added padding below the logo
            )

            // Title with TopAppBar text color and shadow for extra pop
            Text(
                text = "SchoolConnect",
                style = TextStyle(
                    fontSize = 40.sp, // Increased font size
                    fontWeight = FontWeight.Bold,
                    color = topAppBarTextColor,
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.25f),
                        offset = Offset(2f, 2f),
                        blurRadius = 4f
                    )
                ),
                modifier = Modifier.padding(bottom = 32.dp) // Added padding below the title
            )

            // Styled Email TextField
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email", color = topAppBarTextColor) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedTextColor = topAppBarTextColor,
                    focusedBorderColor = topAppBarTextColor,
                    unfocusedBorderColor = topAppBarTextColor.copy(alpha = 0.7f),
                    focusedLabelColor = topAppBarTextColor,
                    unfocusedLabelColor = topAppBarTextColor.copy(alpha = 0.7f),
                    cursorColor = topAppBarTextColor
                ),
                shape = RoundedCornerShape(8.dp) // Rounded corners for the text field
            )

            // Styled Password TextField
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password", color = topAppBarTextColor) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedTextColor = topAppBarTextColor,
                    focusedBorderColor = topAppBarTextColor,
                    unfocusedBorderColor = topAppBarTextColor.copy(alpha = 0.7f),
                    focusedLabelColor = topAppBarTextColor,
                    unfocusedLabelColor = topAppBarTextColor.copy(alpha = 0.7f),
                    cursorColor = topAppBarTextColor
                ),
                shape = RoundedCornerShape(8.dp) // Rounded corners for the text field
            )

            // Error Message
            errorMessage?.let {
                Text(
                    text = it,
                    color = topAppBarTextColor,
                    modifier = Modifier.padding(bottom = 16.dp),
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
            }

            // Login Button with enhanced design
            Button(
                onClick = { onLogin(email, password) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp), // Increased height for better touch target
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = topAppBarTextColor,
                    contentColor = topAppBarBackgroundColor
                ),
                shape = RoundedCornerShape(8.dp) // Rounded corners for the button
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = topAppBarBackgroundColor)
                } else {
                    Text(
                        "Login",
                        color = topAppBarBackgroundColor,
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sign Up Text Button with enhanced design
            TextButton(onClick = onSignupClick) {
                Text(
                    "Don't have an account? Sign Up",
                    color = topAppBarTextColor,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        shadow = Shadow(
                            color = Color.Black.copy(alpha = 0.25f),
                            offset = Offset(1f, 1f),
                            blurRadius = 2f
                        )
                    )
                )
            }
        }
    }
}
