package com.chronelab.madas2schoolconnectapp.schoolconnectdatabse.repository

import com.chronelab.madas2schoolconnectapp.schoolconnectdatabse.dao.CommentDao
import com.chronelab.madas2schoolconnectapp.schoolconnectdatabse.dao.PostDao
import com.chronelab.madas2schoolconnectapp.schoolconnectdatabse.entity.Comment
import com.chronelab.madas2schoolconnectapp.schoolconnectdatabse.entity.Post
import kotlinx.coroutines.flow.Flow

class PostRepository(
    private val postDao: PostDao,
    private val commentDao: CommentDao
) : PostRepositoryInterface {

    override fun getAllPostsStream(): Flow<List<Post>> = postDao.getAllPostsStream()

    override fun getPostStream(id: Long): Flow<Post?> = postDao.getPostById(id) // Matching Long type

    override suspend fun insertPost(post: Post) = postDao.insertPost(post)

    override suspend fun deletePost(post: Post) = postDao.deletePost(post)

    override suspend fun updatePost(post: Post) = postDao.updatePost(post)

    override fun getCommentsForPost(postId: Long): Flow<List<Comment>> = commentDao.getCommentsForPost(postId) // Matching Long type

    override suspend fun addComment(comment: Comment) = commentDao.insert(comment)
}
