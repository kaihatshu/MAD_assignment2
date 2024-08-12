package com.chronelab.roomdatabase.roomdatabase.repository

import com.chronelab.roomdatabase.roomdatabase.entity.Comment
import kotlinx.coroutines.flow.Flow

interface CommentRepositoryInterface {
    fun getCommentsForPost(postId: Long): Flow<List<Comment>>
    suspend fun addComment(comment: Comment)
    suspend fun deleteComment(comment: Comment)
    suspend fun updateComment(comment: Comment)
}
