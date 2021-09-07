package com.example.secondapp.interfaces

import android.os.Bundle

interface LoadingInterface {
    abstract val loading_image: Any
    abstract val loading_relative: Any

    fun openWebView(url:String)
    fun onCreate(savedInstanceState: Bundle?)
    abstract fun requireContext(): Any
}
