package com.example.secondapp.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.secondapp.models.Auth

@Dao
interface AuthDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addData(auth :Auth)

    @Query("DELETE FROM auth")
    fun deleteAll()

    @Query("SELECT * FROM auth")
    fun getAll() : List<Auth>
}