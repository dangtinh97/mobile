package com.example.secondapp.fragments.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.room.Room
import com.example.secondapp.*

import com.example.secondapp.data.BaseResponseObject

import com.example.secondapp.data.UserViewModel
import com.example.secondapp.data.auth.AuthClient
import com.example.secondapp.databases.AuthDatabase
import com.example.secondapp.models.Auth

import kotlinx.android.synthetic.main.fragment_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class LoginFragment : Fragment()   {

    private lateinit var mUserViewModel: UserViewModel


    private val client: AuthClient = AppConfig.retrofit.create(AuthClient::class.java)

    var buttonFlag:Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val db = Room.databaseBuilder(
            requireContext(),
            AuthDatabase::class.java, "auth"
        ).allowMainThreadQueries().build()
        db.authDao().deleteAll()

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        val btn_login = view.findViewById<Button>(R.id.btn_login)
        val mobile = view.findViewById<EditText>(R.id.mobile).text
        val password = view.findViewById<EditText>(R.id.auth_password).text
        btn_login.setOnClickListener{
            if(buttonFlag) return@setOnClickListener
            if(mobile.isEmpty() || password.isEmpty()){
                Toast.makeText(activity,"Vui lòng điền đầy đủ dữ liệu",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            buttonFlag = true
            fetchData(mobile.toString(),password.toString())
        }
        return view
    }



    private fun fetchData(mobile:String, password:String){
        (activity as AuthActivity).onLoading()
         val service: Call<BaseResponseObject> = client.login(mobile,password)
        service.enqueue(object: Callback<BaseResponseObject> {
            override fun onResponse(call: Call<BaseResponseObject>, response: Response<BaseResponseObject>) {
                buttonFlag = false
                if(response.isSuccessful){
                    (activity as AuthActivity).closeLoading()
                    val body = response.body()
                    if(body?.status!=200){
                        Toast.makeText(context,response.body()?.content,Toast.LENGTH_LONG).show()
                        return
                    }
                    val data = body.data
                    val userId = data?.get("user_id").toString()
                    val mobile_number = data?.get("mobile").toString().trim('"')
                    val userOid = data?.get("user_oid").toString().trim('"')
                    val my_referral_code = data?.get("my_referral_code").toString().trim('"')
                    val token = data?.get("token").toString().trim('"')
                    val shortToken = data?.get("short_token").toString().trim('"')
                    val auth = Auth(0,Integer.parseInt(userId),mobile_number,userOid,my_referral_code,token,shortToken)

                    val db = Room.databaseBuilder(
                        requireContext(),
                        AuthDatabase::class.java, "auth"
                    ).allowMainThreadQueries().build()

                    db.authDao().addData(auth)
                    println(db.authDao().getAll()[0].user_id)
                    val myIntent = Intent(activity, VinalifeActivity::class.java)
                    startActivity(myIntent)

                }

            }

            override fun onFailure(call: Call<BaseResponseObject>, t: Throwable) {
                Toast.makeText(activity,t.toString(),Toast.LENGTH_SHORT).show()
            }

        })
    }

}