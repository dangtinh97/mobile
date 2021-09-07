package com.example.secondapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "table_user"
)
data class User (
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    val first_name:String,
    val full_name:String,
    val age:Int
//    val user_id : Int,
//    val mobile : String,
//    val user_oid : String,
//    val my_referral_code : String = "",
//    val token:String = "",
//    val short_token:String = "",
        )