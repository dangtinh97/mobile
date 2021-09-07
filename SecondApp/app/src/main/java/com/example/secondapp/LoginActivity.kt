package com.example.secondapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.attemptLogin
import kotlinx.android.synthetic.main.login_main.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val usernameTrue = "0372052643"
        val passwordTrue = "123456"
        attemptLogin.setOnClickListener{
            val username = login_username.text.toString().trim();
            val password = login_password.text.toString().trim();
            println("--username---- $username -------")
            println("--password---- $password --------")
            if(username.isEmpty() || password.isEmpty() ){
                Toast.makeText(applicationContext,"Vui lòng điền đủ thông tin",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if(username != usernameTrue || password != passwordTrue ){
                Toast.makeText(applicationContext,"Thông tin đăng nhập không chính xác",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            } else{
                val myIntent = Intent(this, MainActivity::class.java)
                startActivity(myIntent)
            }

        }

    }
}