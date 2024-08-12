package com.chronelab.roomdatabase

import android.app.Application
import com.chronelab.roomdatabase.roomdatabase.DatabaseDataContainer
import com.chronelab.roomdatabase.roomdatabase.NoteDatabase

import com.chronelab.roomdatabase.roomdatabase.repository.UserRepository

class RoomApplication : Application() {

    lateinit var databaseContainer: DatabaseDataContainer
    lateinit var userRepository: UserRepository

    override fun onCreate() {
        super.onCreate()
        databaseContainer = DatabaseDataContainer(this)

        // Initialize the database and userRepository
        val database = NoteDatabase.getDatabase(this)
        userRepository = UserRepository(database.userDao())
    }
}
