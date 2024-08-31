package com.chronelab.madas2schoolconnectapp




import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.lifecycleScope
import com.chronelab.madas2schoolconnectapp.ui.theme.HomeBottomNavigation

import com.chronelab.madas2schoolconnectapp.ui.theme.madas2schoolconnectappTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NotificationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            madas2schoolconnectappTheme {
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


        return true
    }

    private fun displayNotification(title: String, message: String) {


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
)

}

@Composable
fun NotificationContent(padding: PaddingValues) {

}
