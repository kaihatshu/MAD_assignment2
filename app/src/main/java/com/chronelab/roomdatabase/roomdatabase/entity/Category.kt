package com.chronelab.roomdatabase.roomdatabase.entity



import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category_table")
class Category (


    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String

    )
