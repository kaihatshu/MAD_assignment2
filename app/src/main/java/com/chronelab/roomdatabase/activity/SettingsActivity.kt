package com.chronelab.roomdatabase.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*

import androidx.compose.ui.Modifier

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import com.chronelab.roomdatabase.ui.theme.RoomDatabaseTheme


class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RoomDatabaseTheme {
                SettingsScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    val settings = listOf("Account", "Privacy", "Notifications", "Theme")

    Scaffold(
        topBar = { TopAppBar(title = { Text("Settings") }) },
        bottomBar = { HomeBottomNavigation(currentRoute = "settings") }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            items(settings) { setting ->
                ListItem(
                    headlineContent = { Text(setting) },
                    trailingContent = { Icon(Icons.Default.KeyboardArrowRight, contentDescription = null) }
                )
                Divider()
            }
        }
    }
}