package com.chronelab.roomdatabase.roomdatabase.repository



import com.chronelab.roomdatabase.model.User

interface UserRepositoryInterface {
    suspend fun insertUser(user: User)
    suspend fun getUser(email: String, password: String): User?
    suspend fun getAllUsers(): List<User>
}
