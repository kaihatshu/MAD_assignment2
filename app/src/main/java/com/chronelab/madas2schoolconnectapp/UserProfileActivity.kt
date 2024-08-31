package com.chronelab.madas2schoolconnectapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chronelab.madas2schoolconnectapp.model.SessionManager
import com.chronelab.madas2schoolconnectapp.model.User
import com.chronelab.madas2schoolconnectapp.ui.theme.HomeBottomNavigation
import com.chronelab.madas2schoolconnectapp.ui.theme.madas2schoolconnectappTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class UserProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sessionManager = SessionManager(this)

        setContent {
            madas2schoolconnectappTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    UserProfileScreen(sessionManager)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(sessionManager: SessionManager) {
    var isEditing by remember { mutableStateOf(false) }
    val user = remember { mutableStateOf(sessionManager.getUser()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("User Profile") },
                actions = {
                    if (!isEditing) {
                        IconButton(onClick = { isEditing = true }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit Profile")
                        }
                    }
                }
            )
        },
        bottomBar = { HomeBottomNavigation(currentRoute = "userProfile") }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (user.value == null) {
                LoadingScreen()
            } else if (isEditing) {
                EditProfileScreen(user.value!!, sessionManager) {
                    isEditing = false
                    user.value = sessionManager.getUser() // Refresh the user data after editing
                }
            } else {
                DisplayProfileScreen(user.value!!)
            }
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun DisplayProfileScreen(user: User) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            ProfileField("Username", user.username)
            ProfileField("Email", user.email)
            ProfileField("Date of Birth", user.dateOfBirth)
            ProfileField("Role", user.role.name)
        }
    }
}

@Composable
fun ProfileField(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(text = label, style = MaterialTheme.typography.labelMedium)
        Text(text = value, style = MaterialTheme.typography.bodyLarge)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(user: User, sessionManager: SessionManager, onEditComplete: () -> Unit) {
    var username by remember { mutableStateOf(user.username) }
    var email by remember { mutableStateOf(user.email) }
    var dateOfBirth by remember { mutableStateOf(user.dateOfBirth) }
    val selectedDate by remember { mutableStateOf(LocalDate.parse(user.dateOfBirth)) }
    var showDatePicker by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = dateOfBirth,
            onValueChange = { },
            label = { Text("Date of Birth") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDatePicker = true }) {
                    Icon(Icons.Default.Edit, contentDescription = "Select date")
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                val updatedUser = user.copy(
                    username = username,
                    email = email,
                    dateOfBirth = dateOfBirth
                )
                sessionManager.startSession(updatedUser)
                onEditComplete()
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Save Changes")
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    dateOfBirth = selectedDate.format(DateTimeFormatter.ISO_LOCAL_DATE)
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(
                state = rememberDatePickerState(initialSelectedDateMillis = selectedDate.toEpochDay() * 24 * 60 * 60 * 1000),
                showModeToggle = false
            )
        }
    }
}
