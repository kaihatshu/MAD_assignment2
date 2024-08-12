package com.chronelab.roomdatabase.model

import androidx.lifecycle.ViewModel

class SessionViewModel(private val sessionManager: SessionManager) : ViewModel() {
    fun updateLastActivity() {
        sessionManager.updateLastActivity()
    }

    fun isSessionActive(): Boolean {
        return sessionManager.isSessionActive()
    }

    fun startSession(user: User) {
        sessionManager.startSession(user)
    }

    fun endSession() {
        sessionManager.endSession()
    }
}
