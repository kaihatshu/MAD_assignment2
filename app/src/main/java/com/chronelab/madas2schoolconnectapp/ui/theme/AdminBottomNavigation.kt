package com.chronelab.madas2schoolconnectapp.ui.theme


import android.content.Intent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.chronelab.madas2schoolconnectapp.UserProfileActivity
import com.chronelab.madas2schoolconnectapp.activity.HomeActivity

import com.chronelab.madas2schoolconnectapp.activity.SettingsActivity
import com.chronelab.madas2schoolconnectapp.activity.AdminDashboardActivity

@Composable
fun AdminBottomNavigation(currentRoute: String = "home") {
    val context = LocalContext.current

    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = currentRoute == "home",
            onClick = {
                if (currentRoute != "home") {
                    val intent = Intent(context, HomeActivity::class.java)
                    context.startActivity(intent)
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = currentRoute == "profile",
            onClick = {
                if (currentRoute != "profile") {
                    val intent = Intent(context, UserProfileActivity::class.java)

                    context.startActivity(intent)
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Settings, contentDescription = "Settings") },
            label = { Text("Settings") },
            selected = currentRoute == "settings",
            onClick = {
                if (currentRoute != "settings") {
                    val intent = Intent(context, SettingsActivity::class.java)
                    context.startActivity(intent)
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Dashboard, contentDescription = "Dashboard") },
            label = { Text("Dashboard") },
            selected = currentRoute == "dashboard",
            onClick = {
                if (currentRoute != "dashboard") {
                    val intent = Intent(context, AdminDashboardActivity::class.java)
                    context.startActivity(intent)
                }
            }
        )
    }
}
