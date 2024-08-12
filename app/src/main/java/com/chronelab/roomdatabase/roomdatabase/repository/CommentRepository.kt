package com.chronelab.roomdatabase.roomdatabase.repository

import com.chronelab.roomdatabase.roomdatabase.dao.CommentDao
import com.chronelab.roomdatabase.roomdatabase.entity.Comment
import kotlinx.coroutines.flow.Flow

class CommentRepository(
    private val commentDao: CommentDao
) : CommentRepositoryInterface {

    override fun getCommentsForPost(postId: Long): Flow<List<Comment>> = commentDao.getCommentsForPost(postId) // Matching Long type

    override suspend fun addComment(comment: Comment) = commentDao.insert(comment)

    override suspend fun deleteComment(comment: Comment) = commentDao.deleteComment(comment)

    override suspend fun updateComment(comment: Comment) = commentDao.updateComment(comment)
}
