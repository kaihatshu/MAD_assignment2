package com.chronelab.madas2schoolconnectapp.schoolconnectdatabse.repository

import com.chronelab.madas2schoolconnectapp.schoolconnectdatabse.entity.Comment
import com.chronelab.madas2schoolconnectapp.schoolconnectdatabse.entity.Post
import kotlinx.coroutines.flow.Flow

interface PostRepositoryInterface {
    fun getAllPostsStream(): Flow<List<Post>>
    fun getPostStream(id: Long): Flow<Post?>
    suspend fun insertPost(post: Post)
    suspend fun deletePost(post: Post)
    suspend fun updatePost(post: Post)
    fun getCommentsForPost(postId: Long): Flow<List<Comment>>
    suspend fun addComment(comment: Comment)
}
