package com.chronelab.madas2schoolconnectapp.schoolconnectdatabse.repository

import com.chronelab.madas2schoolconnectapp.schoolconnectdatabse.entity.Comment
import kotlinx.coroutines.flow.Flow

interface CommentRepositoryInterface {
    fun getCommentsForPost(postId: Long): Flow<List<Comment>>
    suspend fun addComment(comment: Comment)
    suspend fun deleteComment(comment: Comment)
    suspend fun updateComment(comment: Comment)
}
