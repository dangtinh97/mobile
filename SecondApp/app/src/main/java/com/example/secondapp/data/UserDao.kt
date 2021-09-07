package com.example.secondapp.data

import androidx.lifecycle.LiveData
import androidx.room.*
import retrofit2.http.GET

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: User)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addUser2(user: User)

    @Query("SELECT * FROM table_user ORDER BY id ASC")
    fun readAllData() : LiveData<List<User>>

    @Query("DELETE FROM table_user")
     fun deleteAll()
//    @GET("")
}