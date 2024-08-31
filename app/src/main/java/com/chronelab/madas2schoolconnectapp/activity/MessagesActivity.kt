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
import androidx.compose.ui.unit.dp


import com.chronelab.madas2schoolconnectapp.ui.theme.HomeBottomNavigation
import com.chronelab.madas2schoolconnectapp.ui.theme.madas2schoolconnectappTheme

class MessagesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            madas2schoolconnectappTheme {


                MessagesScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagesScreen() {
    val messages = remember { listOf("Message 1", "Message 2", "Message 3") }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Messages") }) },
        bottomBar = { HomeBottomNavigation(currentRoute = "messages") }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            items(messages) { message ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(message, modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}

