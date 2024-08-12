package com.chronelab.roomdatabase.roomdatabase.repository

import com.chronelab.roomdatabase.roomdatabase.entity.Comment
import com.chronelab.roomdatabase.roomdatabase.entity.Post
import kotlinx.coroutines.flow.Flow

interface PostRepositoryInterface {
    fun getAllPostsStream(): Flow<List<Post>>
    fun getPostStream(id: Long): Flow<Post?> // Change to Long
    suspend fun insertPost(post: Post)
    suspend fun deletePost(post: Post)
    suspend fun updatePost(post: Post)
    fun getCommentsForPost(postId: Long): Flow<List<Comment>> // Change to Long
    suspend fun addComment(comment: Comment)
}
