package com.chronelab.madas2schoolconnectapp.model

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel

class SessionViewModel(private val sessionManager: SessionManager) : ViewModel() {
    @RequiresApi(Build.VERSION_CODES.O)
    fun updateLastActivity() {
        sessionManager.updateLastActivity()
    }

    fun isSessionActive(): Boolean {
        return sessionManager.isSessionActive()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun startSession(user: User) {
        sessionManager.startSession(user)
    }

    fun endSession() {
        sessionManager.endSession()
    }
    fun getUser(): User? {
        return sessionManager.getUser()
    }
}
