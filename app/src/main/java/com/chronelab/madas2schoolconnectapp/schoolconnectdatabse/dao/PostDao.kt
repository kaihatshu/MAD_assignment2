package com.chronelab.madas2schoolconnectapp.schoolconnectdatabse.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.chronelab.madas2schoolconnectapp.schoolconnectdatabse.entity.Post
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {
    @Query("SELECT * FROM posts")
    fun getAllPostsStream(): Flow<List<Post>>

    @Query("SELECT * FROM posts WHERE id = :postId")
    fun getPostById(postId: Long): Flow<Post?>

    @Insert
    suspend fun insertPost(post: Post)

    @Delete
    suspend fun deletePost(post: Post)

    @Update
    suspend fun updatePost(post: Post)
}
