package com.example.secondapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "auth")
data class Auth (
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    val user_id : Int,
    val mobile : String,
    val user_oid : String,
    val my_referral_code : String = "",
    val token:String = "",
    val short_token:String = "",
        )