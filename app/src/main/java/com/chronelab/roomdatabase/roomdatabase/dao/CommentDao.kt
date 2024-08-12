package com.chronelab.roomdatabase.roomdatabase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Delete
import androidx.room.Update
import androidx.room.Query
import com.chronelab.roomdatabase.roomdatabase.entity.Comment
import kotlinx.coroutines.flow.Flow

@Dao
interface CommentDao {
    @Query("SELECT * FROM comments WHERE postId = :postId")
    fun getCommentsForPost(postId: Long): Flow<List<Comment>>

    @Insert
    suspend fun insert(comment: Comment)

    @Delete
    suspend fun deleteComment(comment: Comment)

    @Update
    suspend fun updateComment(comment: Comment)
}
