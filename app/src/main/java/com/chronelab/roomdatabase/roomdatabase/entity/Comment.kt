package com.chronelab.roomdatabase.roomdatabase.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comments")
data class Comment(
    @PrimaryKey(autoGenerate = true) val id: Long = 0, // Auto-generate IDs
    val postId: Long,
    val userId: Long,
    val likeCount: Int = 0,
    val dislikeCount: Int = 0,
    val username: String,
    val content: String,
    val timestamp: Long,
    val likedByCurrentUser: Boolean = false,
    val dislikeByCurrentUser: Boolean = false,
)
