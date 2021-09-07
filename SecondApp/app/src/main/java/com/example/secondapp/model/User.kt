package com.example.secondapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "user"
)
data class User (
    @PrimaryKey val uid:Int,
    @ColumnInfo(name="fullname") val fullname : String?,
    @ColumnInfo(name="jwt") val jwt : String?
)