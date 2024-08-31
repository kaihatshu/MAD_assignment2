package com.chronelab.madas2schoolconnectapp.activity


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.chronelab.madas2schoolconnectapp.RoomApplication
import com.chronelab.madas2schoolconnectapp.model.AdminViewModel
import com.chronelab.madas2schoolconnectapp.model.AdminViewModelFactory
import com.chronelab.madas2schoolconnectapp.model.User
import com.chronelab.madas2schoolconnectapp.model.UserRole
import com.chronelab.madas2schoolconnectapp.ui.theme.madas2schoolconnectappTheme
class AdminDashboardActivity : ComponentActivity() {
    private val adminViewModel: AdminViewModel by viewModels {
        AdminViewModelFactory((application as RoomApplication).userRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            madas2schoolconnectappTheme {
                AdminDashboardContent(adminViewModel)
            }
        }
    }
}

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun AdminDashboardContent(adminViewModel: AdminViewModel) {
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LaunchedEffect(Unit) {
            try {
                adminViewModel.fetchAllUsers()
            } catch (e: Exception) {
                errorMessage = "Failed to load dashboard. Please check your internet connection and Google account."
                Log.e("AdminDashboard", "Error loading dashboard", e)
            }
        }

        if (errorMessage != null) {
            ErrorScreen(message = errorMessage!!) {
                errorMessage = null
                adminViewModel.fetchAllUsers()
            }
        } else {
            AdminDashboardScreen(adminViewModel)
        }
    }
}

@Composable
fun ErrorScreen(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = message, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(adminViewModel: AdminViewModel = viewModel()) {
    val users by adminViewModel.users.collectAsState()
    var selectedUser by remember { mutableStateOf<User?>(null) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Admin Dashboard") }) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(users) { user ->
                UserListItem(
                    user = user,
                    onEditClick = { selectedUser = user },
                    onDeleteClick = { adminViewModel.deleteUser(user) }
                )
            }
        }

        selectedUser?.let { user ->
            EditUserDialog(
                user = user,
                onDismiss = { selectedUser = null },
                onSave = { updatedUser ->
                    adminViewModel.updateUser(updatedUser)
                    selectedUser = null
                }
            )
        }
    }
}

@Composable
fun UserListItem(
    user: User,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Name: ${user.username}")
                Text(text = "Email: ${user.email}")
                Text(text = "Role: ${user.role}")
            }
            IconButton(onClick = onEditClick) {
                Icon(Icons.Default.Edit, contentDescription = "Edit")
            }
            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditUserDialog(
    user: User,
    onDismiss: () -> Unit,
    onSave: (User) -> Unit
) {
    var username by remember { mutableStateOf(user.username) }
    var email by remember { mutableStateOf(user.email) }
    var dateOfBirth by remember { mutableStateOf(user.dateOfBirth) }
    var role by remember { mutableStateOf(user.role) }
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit User") },
        text = {
            Column {
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username") }
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") }
                )
                OutlinedTextField(
                    value = dateOfBirth,
                    onValueChange = { dateOfBirth = it },
                    label = { Text("Date of Birth") }
                )
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = role.name,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Role") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        UserRole.entries.forEach { userRole ->
                            DropdownMenuItem(
                                text = { Text(userRole.name) },
                                onClick = {
                                    role = userRole
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                onSave(user.copy(
                    username = username,
                    email = email,
                    dateOfBirth = dateOfBirth,
                    role = role
                ))
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}