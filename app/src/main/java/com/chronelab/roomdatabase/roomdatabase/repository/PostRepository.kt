package com.chronelab.roomdatabase.roomdatabase.repository

import com.chronelab.roomdatabase.roomdatabase.dao.CommentDao
import com.chronelab.roomdatabase.roomdatabase.dao.PostDao
import com.chronelab.roomdatabase.roomdatabase.entity.Comment
import com.chronelab.roomdatabase.roomdatabase.entity.Post
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
