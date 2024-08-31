package com.chronelab.madas2schoolconnectapp.schoolconnectdatabse

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.chronelab.madas2schoolconnectapp.activity.CommentListConverter
import com.chronelab.madas2schoolconnectapp.schoolconnectdatabse.dao.CommentDao
import com.chronelab.madas2schoolconnectapp.schoolconnectdatabse.dao.PostDao
import com.chronelab.madas2schoolconnectapp.schoolconnectdatabse.dao.UserDao
import com.chronelab.madas2schoolconnectapp.schoolconnectdatabse.entity.Comment
import com.chronelab.madas2schoolconnectapp.schoolconnectdatabse.entity.Post
import com.chronelab.madas2schoolconnectapp.model.User


@Database(entities = [User::class, Post::class, Comment::class], version = 7, exportSchema = false)
@TypeConverters(CommentListConverter::class)
abstract class SchoolDatabase : RoomDatabase() {
    abstract fun commentDao(): CommentDao
    abstract fun postDao(): PostDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: SchoolDatabase? = null

        fun getDatabase(context: Context): SchoolDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SchoolDatabase::class.java,
                    "note_database"
                )
                    .addMigrations(MIGRATION_3_4)
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Adding the 'username' column with a default value
                database.execSQL("ALTER TABLE users ADD COLUMN username TEXT NOT NULL DEFAULT ''")
                database.execSQL("ALTER TABLE PostEntity ADD COLUMN username TEXT")
                database.execSQL("ALTER TABLE CommentEntity ADD COLUMN username TEXT")

            }
        }
    }
}
