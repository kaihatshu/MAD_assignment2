package com.chronelab.madas2schoolconnectapp.model

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.ZoneOffset

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    @RequiresApi(Build.VERSION_CODES.O)
    fun startSession(user: User) {
        prefs.edit().apply {
            putInt("userId", user.id)
            putString("userEmail", user.email)
            putString("username", user.username)
            putString("role", user.role.name)
            putLong("lastActivity", LocalDateTime.now().toEpochSecond(ZoneOffset.UTC))
            apply()
        }
    }

    fun getUser(): User? {
        val id = prefs.getInt("userId", -1)
        val email = prefs.getString("userEmail", null)
        val username = prefs.getString("username", null)
        val roleString = prefs.getString("role", UserRole.STUDENT.name)
        val lastActivity = prefs.getLong("lastActivity", -1L)

        if (id != -1 && email != null && username != null) {
            val role = UserRole.valueOf(roleString!!)
            return User(id, username, "", email, "", role)
        }
        return null
    }

    fun isSessionActive(): Boolean {
        return prefs.contains("userId")
    }

    fun endSession() {
        prefs.edit().clear().apply()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateLastActivity() {
        prefs.edit().apply {
            putLong("lastActivity", LocalDateTime.now().toEpochSecond(ZoneOffset.UTC))
            apply()
        }
    }
}
