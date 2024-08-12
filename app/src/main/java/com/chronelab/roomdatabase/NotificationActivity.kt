package com.chronelab.roomdatabase




import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.lifecycleScope
import com.chronelab.roomdatabase.activity.HomeActivity
import com.chronelab.roomdatabase.activity.HomeBottomNavigation
import com.chronelab.roomdatabase.ui.theme.RoomDatabaseTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NotificationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RoomDatabaseTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NotificationScreen()
                }
            }
        }

        // Show welcome notification on first app launch
        showWelcomeNotification()

        // Show other notifications after 30 seconds
        showOtherNotifications()
    }

    private fun showWelcomeNotification() {
        // Check if this is the user's first time using the app
        if (isFirstLaunch()) {
            // Display a welcome notification
            displayNotification(
                title = "Welcome to SchoolConnect!",
                message = "Thanks for joining our community."
            )
        }
    }

    private fun showOtherNotifications() {
        lifecycleScope.launch {
            delay(10000) // Wait 30 seconds
            // Display notifications for likes, comments, etc.
            displayNotification(
                title = "New Notification",
                message = "Someone liked your post."
            )
            displayNotification(
                title = "New Comment",
                message = "A user commented on your post."
            )
        }
    }

    private fun isFirstLaunch(): Boolean {
        // Implement logic to check if this is the user's first time using the app
        // This could involve storing a flag in shared preferences or a database
        return true // Assuming true for now
    }

    private fun displayNotification(title: String, message: String) {
        // Implement the logic to display a notification
        // This could involve using the Android NotificationManager or a third-party library
        println("Displaying notification: $title - $message")
    }
}

@Composable
fun NotificationScreen() {
    Scaffold(
        topBar = { NotificationTopBar() },
        bottomBar = { HomeBottomNavigation(currentRoute = "notifications") },
        content = { padding -> NotificationContent(padding) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationTopBar() {
    TopAppBar(
        title = { Text("Notifications", fontWeight = FontWeight.Bold) },
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
fun NotificationContent(padding: PaddingValues) {
    // Add notification content here
}