package com.example.secondapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.JsonElement

@Entity(
    tableName = "setup_app"
)
data class SetupApp(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val key:String? = "",
    val data: String?
        )