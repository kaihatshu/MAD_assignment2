package com.chronelab.madas2schoolconnectapp.schoolconnectdatabse.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comments")
data class Comment(
    @PrimaryKey(autoGenerate = true) val id: Long = 0, // The Id are auto generated
    val postId: Long,
    val userId: Int,
    val likeCount: Int = 0,
    val dislikeCount: Int = 0,
    val username: String,
    val content: String,
    val timestamp: Long,
    val likedByCurrentUser: Boolean = false,
    val dislikeByCurrentUser: Boolean = false,
)
