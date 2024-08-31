package com.chronelab.madas2schoolconnectapp.model

import com.chronelab.madas2schoolconnectapp.schoolconnectdatabse.repository.UserRepository



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserProfileViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    fun fetchCurrentUser() {
        viewModelScope.launch {
            try {

                val userId = getCurrentUserId()
                _currentUser.value = userRepository.getUserById(userId)
            } catch (e: Exception) {
                // Handle error
                e.printStackTrace()
            }
        }
    }

    fun updateUser(updatedUser: User) {
        viewModelScope.launch {
            try {
                userRepository.updateUser(updatedUser)
                _currentUser.value = updatedUser
            } catch (e: Exception) {
                // Handle error
                e.printStackTrace()
            }
        }
    }


    private fun getCurrentUserId(): String {


        return "current_user_id"
    }
}
