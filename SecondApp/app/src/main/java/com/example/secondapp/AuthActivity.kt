package com.example.secondapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.RotateAnimation
import android.widget.Toast
import com.example.secondapp.fragments.auth.LoginFragment
import com.example.secondapp.interfaces.LoadingInterface
import kotlinx.android.synthetic.main.activity_auth.*

class AuthActivity : AppCompatActivity() {

    var buttonFlag:Boolean = false

    private val loginFragment = LoginFragment()
    private var onBack:Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        setFragmentLogin()
    }

    private fun setFragmentLogin(){
        //super.onBackPressed()
        onBack = true
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container_view_tag_auth,loginFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onBackPressed() {
        if(onBack){
            moveTaskToBack(true)
            return
        }
    }

    internal fun onLoading(){
        loading_relative.visibility = View.VISIBLE
        loading_image.animate().apply {
            duration = 10000
            rotation(3600f)
        }.start()
    }

    internal fun closeLoading(){
        loading_image.animate().apply {
            duration = 10000
            rotation(3600f)
        }.cancel()
        loading_relative.visibility = View.GONE
    }


}