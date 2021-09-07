package com.example.secondapp.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.secondapp.daos.SetupAppDao
import com.example.secondapp.models.SetupApp

@Database(entities = [SetupApp::class],version = 1,exportSchema = false)
abstract class SetupAppDatabase:RoomDatabase() {
    abstract fun setUpAppDao(): SetupAppDao

    companion object{
        @Volatile
        private var INSTANCE: SetupAppDatabase? = null

        fun getDatabase(context: Context): SetupAppDatabase {
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SetupAppDatabase::class.java,
                    "setup_app"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }



}