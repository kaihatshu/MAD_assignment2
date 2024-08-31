package com.chronelab.madas2schoolconnectapp.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.chronelab.madas2schoolconnectapp.RoomApplication
import com.chronelab.madas2schoolconnectapp.model.User
import com.chronelab.madas2schoolconnectapp.model.UserRole
import com.chronelab.madas2schoolconnectapp.schoolconnectdatabse.repository.UserRepositoryInterface
import com.chronelab.madas2schoolconnectapp.ui.theme.madas2schoolconnectappTheme
import kotlinx.coroutines.launch

class RegistrationActivity : ComponentActivity() {
    private lateinit var userRepository: UserRepositoryInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize userRepository from the application
        userRepository = (application as RoomApplication).userRepository

        setContent {
            madas2schoolconnectappTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color(0xFFE91E63) // Background color for Scaffold to match TopAppBar
                ) { innerPadding ->
                    RegistrationScreen(
                        onRegister = { user ->
                            lifecycleScope.launch {
                                try {
                                    userRepository.insertUser(user.copy(role = UserRole.STUDENT))
                                    // Optionally navigate to login or home screen
                                    finish()
                                } catch (e: Exception) {

                                }
                            }
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(onRegister: (User) -> Unit, modifier: Modifier = Modifier) {
    var fullName by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val backgroundColor = Color(0xFFE91E63) // Matching the color used in TopAppBar
    val textColor = Color.White // Text color for consistency

    // Function to validate date format
    fun isValidDate(date: String): Boolean {
        return date.matches(Regex("\\d{4}-\\d{2}-\\d{2}")) // YYYY-MM-DD format
    }

    // Function to validate email
    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Function to validate password length
    fun isValidPassword(password: String): Boolean {
        return password.length > 3
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Register",
                style = TextStyle(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("Full Name", color = textColor) },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedTextColor = textColor,
                    focusedBorderColor = textColor,
                    unfocusedBorderColor = textColor.copy(alpha = 0.7f),
                    focusedLabelColor = textColor,
                    unfocusedLabelColor = textColor.copy(alpha = 0.7f),
                    cursorColor = textColor
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = dateOfBirth,
                onValueChange = { dateOfBirth = it },
                label = { Text("Date of Birth (YYYY-MM-DD)", color = textColor) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedTextColor = textColor,
                    focusedBorderColor = textColor,
                    unfocusedBorderColor = textColor.copy(alpha = 0.7f),
                    focusedLabelColor = textColor,
                    unfocusedLabelColor = textColor.copy(alpha = 0.7f),
                    cursorColor = textColor
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email", color = textColor) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedTextColor = textColor,
                    focusedBorderColor = textColor,
                    unfocusedBorderColor = textColor.copy(alpha = 0.7f),
                    focusedLabelColor = textColor,
                    unfocusedLabelColor = textColor.copy(alpha = 0.7f),
                    cursorColor = textColor
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password", color = textColor) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedTextColor = textColor,
                    focusedBorderColor = textColor,
                    unfocusedBorderColor = textColor.copy(alpha = 0.7f),
                    focusedLabelColor = textColor,
                    unfocusedLabelColor = textColor.copy(alpha = 0.7f),
                    cursorColor = textColor
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Display validation errors
            errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(vertical = 8.dp),
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = textColor
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    when {
                        !isValidDate(dateOfBirth) -> {
                            errorMessage = "Date must be in YYYY-MM-DD format"
                        }
                        !isValidEmail(email) -> {
                            errorMessage = "Invalid email address"
                        }
                        !isValidPassword(password) -> {
                            errorMessage = "Password must be more than 3 characters"
                        }
                        fullName.isNotEmpty() && dateOfBirth.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() -> {
                            val user = User(username = fullName, dateOfBirth = dateOfBirth, email = email, role = UserRole.STUDENT, password = password)
                            onRegister(user)
                        }
                        else -> {
                            errorMessage = "Please fill all fields"
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = backgroundColor
                )
            ) {
                Text(text = "Register")
            }
        }
    }
}
