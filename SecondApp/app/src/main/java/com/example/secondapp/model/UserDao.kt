package com.example.secondapp.model

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("SELECT * FROM user")
    fun getAlphabetizedUids(): Flow<List<User>>

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<User>

//    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    fun findByName(first: String, last: String): User

//    @Insert
//    fun insertAll(vararg users: User)

    @Delete
    fun delete(user: User)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User)
}