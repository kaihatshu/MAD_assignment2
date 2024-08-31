package com.chronelab.madas2schoolconnectapp.schoolconnectdatabse.repository



import com.chronelab.madas2schoolconnectapp.model.User

interface UserRepositoryInterface {
    suspend fun insertUser(user: User)
    suspend fun getUser(email: String, password: String): User?
    suspend fun getAllUsers(): List<User>
    suspend fun getUserById(userId: String): User?
    suspend fun updateUser(user: User)
    suspend fun getUserByEmail(email: String): User?
}


