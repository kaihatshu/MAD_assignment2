package com.chronelab.roomdatabase.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.chronelab.roomdatabase.RoomApplication
import com.chronelab.roomdatabase.model.SessionManager
import com.chronelab.roomdatabase.model.User
import com.chronelab.roomdatabase.roomdatabase.repository.UserRepositoryInterface
import com.chronelab.roomdatabase.ui.theme.RoomDatabaseTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : ComponentActivity() {
    private lateinit var userRepository: UserRepositoryInterface
    private lateinit var sessionManager: SessionManager

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

        // Check for existing session
        if (sessionManager.isSessionActive()) {
            val user = sessionManager.getUser()
            if (user != null) {
                Log.d("LoginActivity", "Active session found, navigating to HomeActivity")
                navigateToHome(user)
                return
            }
        }

        setContent {
            RoomDatabaseTheme {
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
                    Log.i("LoginActivity", "Login successful for ${user.email}")
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

@Composable
fun LoginScreen(
    onLogin: (String, String) -> Unit,
    onSignupClick: () -> Unit,
    errorMessage: String?,
    isLoading: Boolean
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { onLogin(email, password) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
            } else {
                Text("Login")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onSignupClick) {
            Text("Don't have an account? Sign Up")
        }
    }
}
