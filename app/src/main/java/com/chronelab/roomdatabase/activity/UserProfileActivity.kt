package com.chronelab.roomdatabase.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import com.chronelab.roomdatabase.ui.theme.RoomDatabaseTheme


class UserProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RoomDatabaseTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    UserProfileScreen()
                }
            }
        }
    }
}

@Composable
fun UserProfileScreen() {
    Scaffold(
        topBar = { UserProfileTopBar() },
        bottomBar = { HomeBottomNavigation(currentRoute = "profile") },
        content = { padding -> UserProfileContent(padding) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun  UserProfileTopBar() {
    TopAppBar(
        title = { Text("Profile", fontWeight = FontWeight.Bold) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFFE91E63),
            titleContentColor = Color.White,
            actionIconContentColor = Color.White
        ),
        navigationIcon = {
            val context = LocalContext.current
            IconButton(onClick = { context.startActivity(Intent(context, HomeActivity::class.java)) }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back to Home")
            }
        }
    )
}

@Composable
fun UserProfileContent(padding: PaddingValues) {
    // Add profile content here
}