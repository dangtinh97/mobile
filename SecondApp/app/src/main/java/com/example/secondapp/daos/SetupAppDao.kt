package com.example.secondapp.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.secondapp.models.SetupApp

@Dao
interface SetupAppDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addData(setupApp: SetupApp)

    @Query("DELETE FROM setup_app")
    fun deleteAll()

    @Query("SELECT * FROM setup_app")
    fun getAll() : List<SetupApp>
}