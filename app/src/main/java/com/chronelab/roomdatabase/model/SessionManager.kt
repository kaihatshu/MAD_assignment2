package com.chronelab.roomdatabase.model

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("SessionPrefs", Context.MODE_PRIVATE)
    private val sessionTimeout: Long = 15 * 60 * 1000 // 15 minutes in milliseconds

    fun startSession(user: User) {
        with(sharedPreferences.edit()) {
            putLong("userId", user.id.toLong()) // Convert userId to Long
            putString("userEmail", user.email)
            putString("username", user.username)
            putLong("lastActivity", System.currentTimeMillis())
            apply()
        }
    }

    fun endSession() {
        sharedPreferences.edit().clear().apply()
    }

    fun isSessionActive(): Boolean {
        val lastActivity = sharedPreferences.getLong("lastActivity", 0L)
        return System.currentTimeMillis() - lastActivity < sessionTimeout
    }

    fun updateLastActivity() {
        sharedPreferences.edit().putLong("lastActivity", System.currentTimeMillis()).apply()
    }

    fun getUser(): User? {
        val userId = sharedPreferences.getLong("userId", -1L) // Change to Long
        val userEmail = sharedPreferences.getString("userEmail", null)
        val username = sharedPreferences.getString("username", null)

        return if (userId != -1L && userEmail != null && username != null) {
            User(id = userId.toLong(), username = username, dateOfBirth = "", email = userEmail, password = "")
        } else {
            null
        }
    }
}
