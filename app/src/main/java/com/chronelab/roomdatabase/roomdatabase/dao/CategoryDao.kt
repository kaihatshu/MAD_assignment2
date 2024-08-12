package com.chronelab.roomdatabase.roomdatabase.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.chronelab.roomdatabase.roomdatabase.entity.Category
import kotlinx.coroutines.flow.Flow

interface CategoryDao {   @Insert(onConflict = OnConflictStrategy.IGNORE)
suspend fun insert(category: Category)

    @Update
    suspend fun update(category: Category)

    @Delete
    suspend fun delete(category: Category)

    @Query("SELECT * from category_table WHERE id = :id")
    fun getItem(id: Int): Flow<Category>

    @Query("SELECT * from category_table ORDER BY name ASC")
    fun getAllItems(): Flow<List<Category>>
}


