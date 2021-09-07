package com.example.secondapp.data

import androidx.lifecycle.LiveData

class UserRepository(private val userDao: UserDao) {
    val readAllData : LiveData<List<User>> = userDao.readAllData()

    suspend fun addUser(user: User){
        userDao.addUser(user)
    }

    fun addUser2(user: User){
        userDao.addUser2(user)
    }

     fun deleteAll(){
        userDao.deleteAll()
    }
}
