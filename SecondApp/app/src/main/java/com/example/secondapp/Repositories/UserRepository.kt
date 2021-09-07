package com.example.secondapp.Repositories

import androidx.annotation.WorkerThread
import com.example.secondapp.model.User
import com.example.secondapp.model.UserDao
import kotlinx.coroutines.flow.Flow

class UserRepository (private val userDao: UserDao) {
    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allWords: Flow<List<User>> = userDao.getAlphabetizedUids()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(user: User) {
        userDao.insert(user)
    }
}