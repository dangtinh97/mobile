package com.example.reviewphim.services

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences


const val MY_SHARED_PREFERENCES = "MY_SHARED_PREFERENCES";

class SharedPreferencesService {


    private lateinit var activity: Activity

    constructor(activity: Activity){
        this.activity = activity
    }

    fun getBoolean(key:String): Boolean {
        return activity.getSharedPreferences(MY_SHARED_PREFERENCES,Context.MODE_PRIVATE).getBoolean(key,false)
    }

    fun setBoolean(key:String,value:Boolean){
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putBoolean(key,value)
            apply()
        }
    }

    fun getString(key:String):String
    {
        return activity.getSharedPreferences(MY_SHARED_PREFERENCES,Context.MODE_PRIVATE).getString(key,"").toString()
    }

    fun setString(key:String,value:String){
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putString(key,value)
            apply()
        }
    }


}
