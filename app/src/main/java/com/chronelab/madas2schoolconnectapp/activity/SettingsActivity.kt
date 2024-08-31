package com.chronelab.madas2schoolconnectapp.activity


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier



import com.chronelab.madas2schoolconnectapp.ui.theme.HomeBottomNavigation
import com.chronelab.madas2schoolconnectapp.ui.theme.madas2schoolconnectappTheme

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            madas2schoolconnectappTheme {
                SettingsScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    val settings = listOf("Account", "Privacy", "Notifications", "Theme", "Admin Registration")


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



            }
        }
    }
}
