package com.example.secondapp.Repositories

import com.example.secondapp.daos.AuthDao
import com.example.secondapp.models.Auth

class AuthRepository (private val authDao: AuthDao) {

    suspend fun addData(auth: Auth){
        authDao.addData(auth)
    }

    fun deleteAll(){
        authDao.deleteAll()
    }
}