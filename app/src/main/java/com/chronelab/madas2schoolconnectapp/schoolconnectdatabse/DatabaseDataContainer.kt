package com.chronelab.madas2schoolconnectapp.schoolconnectdatabse

import android.content.Context

import com.chronelab.madas2schoolconnectapp.schoolconnectdatabse.repository.CommentRepository
import com.chronelab.madas2schoolconnectapp.schoolconnectdatabse.repository.CommentRepositoryInterface
import com.chronelab.madas2schoolconnectapp.schoolconnectdatabse.repository.PostRepository
import com.chronelab.madas2schoolconnectapp.schoolconnectdatabse.repository.PostRepositoryInterface
import com.chronelab.madas2schoolconnectapp.schoolconnectdatabse.repository.UserRepository
import com.chronelab.madas2schoolconnectapp.schoolconnectdatabse.repository.UserRepositoryInterface

interface DatabaseContainer {
    val postRepositoryInterface: PostRepositoryInterface
    val commentRepositoryInterface: CommentRepositoryInterface
    val userRepositoryInterface: UserRepositoryInterface
}

class DatabaseDataContainer(context: Context) : DatabaseContainer {

    private val database = SchoolDatabase.getDatabase(context)

    override val postRepositoryInterface: PostRepositoryInterface by lazy {
        PostRepository(database.postDao(), database.commentDao())
    }

    override val commentRepositoryInterface: CommentRepositoryInterface by lazy {
        CommentRepository(database.commentDao())
    }

    override val userRepositoryInterface: UserRepositoryInterface by lazy {
        UserRepository(database.userDao())
    }
}