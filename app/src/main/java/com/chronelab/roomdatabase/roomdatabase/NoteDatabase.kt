package com.chronelab.roomdatabase.roomdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.chronelab.roomdatabase.activity.CommentListConverter
import com.chronelab.roomdatabase.model.User
import com.chronelab.roomdatabase.roomdatabase.dao.CommentDao
import com.chronelab.roomdatabase.roomdatabase.dao.PostDao
import com.chronelab.roomdatabase.roomdatabase.dao.UserDao
import com.chronelab.roomdatabase.roomdatabase.entity.Comment

import com.chronelab.roomdatabase.roomdatabase.entity.Post

@Database(entities = [User::class, Post::class, Comment::class], version = 5, exportSchema = false)
@TypeConverters(CommentListConverter::class)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun commentDao(): CommentDao
    abstract fun postDao(): PostDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: NoteDatabase? = null

        fun getDatabase(context: Context): NoteDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java,
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
