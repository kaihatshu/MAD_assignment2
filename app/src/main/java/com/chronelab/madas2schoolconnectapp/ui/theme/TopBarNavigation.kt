package com.chronelab.madas2schoolconnectapp.ui.theme

import android.content.Intent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Notifications

import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import com.chronelab.madas2schoolconnectapp.NotificationActivity
import com.chronelab.madas2schoolconnectapp.UserProfileActivity
import com.chronelab.madas2schoolconnectapp.activity.SearchActivity


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarNavigation(logout: () -> Unit) {
    val context = LocalContext.current

    TopAppBar(
        title = { Text("SchoolConnect", fontWeight = FontWeight.Bold) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFFE91E63),
            titleContentColor = Color.White,
            actionIconContentColor = Color.White
        ),
        actions = {
            IconButton(onClick = {
                context.startActivity(Intent(context, NotificationActivity::class.java))
            }) {
                Icon(Icons.Filled.Notifications, contentDescription = "Notifications")
            }
            IconButton(onClick = {
                context.startActivity(Intent(context, SearchActivity::class.java))
            }) {
                Icon(Icons.Filled.Search, contentDescription = "Search")
            }

            IconButton(onClick = logout) {
                Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Logout")
            }
        }
    )
}