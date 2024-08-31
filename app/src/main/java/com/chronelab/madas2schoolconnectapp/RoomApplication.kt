package com.chronelab.madas2schoolconnectapp

import android.app.Application
import com.chronelab.madas2schoolconnectapp.model.User
import com.chronelab.madas2schoolconnectapp.model.UserRole

import com.chronelab.madas2schoolconnectapp.schoolconnectdatabse.DatabaseDataContainer
import com.chronelab.madas2schoolconnectapp.schoolconnectdatabse.SchoolDatabase


import com.chronelab.madas2schoolconnectapp.schoolconnectdatabse.repository.UserRepository
import kotlinx.coroutines.runBlocking

class RoomApplication : Application() {

    lateinit var databaseContainer: DatabaseDataContainer
    lateinit var userRepository: UserRepository

    override fun onCreate() {
        super.onCreate()
        databaseContainer = DatabaseDataContainer(this)

        // Initialize the database and userRepository
        val database = SchoolDatabase.getDatabase(this)
        userRepository = UserRepository(database.userDao())

        // Create default admin user
        runBlocking {
            createDefaultAdminUser()
        }
    }

    private suspend fun createDefaultAdminUser() {
        val adminEmail = "admin@gmail.com"
        val adminPassword = "Admin@1"

        val existingAdmin = userRepository.getUser(adminEmail, adminPassword)
        if (existingAdmin == null) {
            val adminUser = User(
                username = "Admin",
                email = adminEmail,
                password = adminPassword,
                role = UserRole.ADMIN,
                dateOfBirth = "01/01/1990" // You can change this as needed
            )
            userRepository.insertUser(adminUser)
        }
    }
}


