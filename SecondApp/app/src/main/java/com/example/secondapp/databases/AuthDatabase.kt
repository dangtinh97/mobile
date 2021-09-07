package com.example.secondapp.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.secondapp.daos.AuthDao
import com.example.secondapp.data.UserDatabase
import com.example.secondapp.models.Auth

@Database(entities = [Auth::class],version = 1,exportSchema = false)
abstract class AuthDatabase : RoomDatabase() {
    abstract fun authDao():AuthDao

    companion object{
        @Volatile
        private var INSTANCE: AuthDatabase? = null

        fun getDatabase(context: Context): AuthDatabase {
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AuthDatabase::class.java,
                    "auth"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}