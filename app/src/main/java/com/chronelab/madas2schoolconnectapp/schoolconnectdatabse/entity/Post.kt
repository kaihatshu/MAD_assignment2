package com.chronelab.madas2schoolconnectapp.schoolconnectdatabse.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

import androidx.room.TypeConverters

import com.chronelab.madas2schoolconnectapp.activity.CommentListConverter


@Entity(tableName = "posts") // table name for post
data class Post(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Int,
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

