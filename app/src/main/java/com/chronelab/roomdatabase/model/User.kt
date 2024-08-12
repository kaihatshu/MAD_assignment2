package com.chronelab.roomdatabase.model

import java.io.Serializable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    var username: String,
    var dateOfBirth: String,
    var email: String,
    val password: String = "",
) : Serializable