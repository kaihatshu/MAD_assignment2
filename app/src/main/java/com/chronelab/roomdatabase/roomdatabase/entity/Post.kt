package com.chronelab.roomdatabase.roomdatabase.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

import androidx.room.TypeConverters

import com.chronelab.roomdatabase.activity.CommentListConverter


@Entity(tableName = "posts") // Make sure this matches the table name used in queries
data class Post(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val content: String,
    val username: String,
    val timestamp: Long,
    val likeCount: Int = 0,
    val dislikeCount: Int = 0,
    val likedByCurrentUser: Boolean = false,
    val dislikeByCurrentUser: Boolean = false,

    @ColumnInfo(name = "comments")
    @TypeConverters(CommentListConverter::class)
    val comments: List<Comment> = emptyList()
)

