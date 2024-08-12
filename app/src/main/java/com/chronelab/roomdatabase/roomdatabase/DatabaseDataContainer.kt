package com.chronelab.roomdatabase.roomdatabase

import android.content.Context
import com.chronelab.roomdatabase.roomdatabase.repository.CommentRepository
import com.chronelab.roomdatabase.roomdatabase.repository.CommentRepositoryInterface
import com.chronelab.roomdatabase.roomdatabase.repository.PostRepository
import com.chronelab.roomdatabase.roomdatabase.repository.PostRepositoryInterface
import com.chronelab.roomdatabase.roomdatabase.repository.UserRepository
import com.chronelab.roomdatabase.roomdatabase.repository.UserRepositoryInterface

interface DatabaseContainer {
    val postRepositoryInterface: PostRepositoryInterface
    val commentRepositoryInterface: CommentRepositoryInterface
    val userRepositoryInterface: UserRepositoryInterface
}

class DatabaseDataContainer(context: Context) : DatabaseContainer {

    private val database = NoteDatabase.getDatabase(context)

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