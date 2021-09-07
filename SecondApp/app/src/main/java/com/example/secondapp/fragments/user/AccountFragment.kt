package com.example.secondapp.fragments.user

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.bumptech.glide.Glide
import com.example.secondapp.AppConfig
import com.example.secondapp.AuthActivity
import com.example.secondapp.R
import com.example.secondapp.VinalifeActivity
import com.example.secondapp.adapters.AccountOtherAdapter
import com.example.secondapp.data.BaseResponseObject
import com.example.secondapp.data.auth.AuthClient
import com.example.secondapp.databases.AuthDatabase
import com.example.secondapp.interfaces.LoadingInterface
import com.example.secondapp.models.AccountListInOther
import com.example.secondapp.models.AccountOther
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import kotlinx.android.synthetic.main.activity_demo.*
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.fragment_account.view.*
import kotlinx.android.synthetic.main.item_other_account.view.*
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ObjectOutput
import java.util.*
import java.util.stream.IntStream
import kotlin.collections.ArrayList

class AccountFragment (private val vinalifeActivity: VinalifeActivity) : LoadingInterface, Fragment() {

    private val client: AuthClient = AppConfig.retrofit.create(AuthClient::class.java)
    private var buttonFlag:Boolean = false
    lateinit var accountOtherAdapter: AccountOtherAdapter

    var objectData:ArrayList<AccountOther> = arrayListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_account, container, false)
        view.logout.setOnClickListener{
            logout()
        }

        fetchData(view)

//        view.relative_layout_account_user_other_item.setOnClickListener {
//            Toast.makeText(requireContext(),"hello",Toast.LENGTH_LONG).show()
//        }

        return view
    }

    private fun fetchData(view: View){
        (activity as VinalifeActivity).onLoading()
        val db = Room.databaseBuilder(
            requireContext(),
            AuthDatabase::class.java, "auth"
        ).allowMainThreadQueries().build()
        db.authDao().getAll()[0].token

        val authorization:String = db.authDao().getAll()[0].token
        println("bearer ${authorization.trim('"')}")
        val service: Call<BaseResponseObject> = client.account("bearer ${authorization.trim('"')}")

        service.enqueue(object: Callback<BaseResponseObject> {
            override fun onResponse(call: Call<BaseResponseObject>, response: Response<BaseResponseObject>) {
                buttonFlag = false

                if(response.isSuccessful){
                    (activity as VinalifeActivity).closeLoading()
                    val body = response.body()

                    if(body?.status!=200){
                        Toast.makeText(context,response.body()?.content, Toast.LENGTH_LONG).show()
                        return
                    }

                    val data = body.data
                     val user = data?.get("user").toString()
                    val others = data?.get("other").toString()
                    println(data)
                    //val fullname:String = user.fullname
                    if (user != "" && others.isNotEmpty()) {
                        val jsonObject = JSONTokener(user).nextValue() as JSONObject
                        parseInfo(jsonObject,view)
                    }

                    if(others != "" && others.isNotEmpty()){
                        val jsonArray = Gson().fromJson(others, mutableListOf<String>().javaClass)

                        parseList(jsonArray,view)
                        //println(jsonArray[0])

                    }



//                    val userId = data?.get("user_id").toString()
//                    val mobile_number = data?.get("mobile").toString()
//                    val userOid = data?.get("user_oid").toString()
//                    val my_referral_code = data?.get("my_referral_code").toString()
//                    val token = data?.get("token").toString()
//                    val shortToken = data?.get("short_token").toString()
//                    val auth = Auth(0,Integer.parseInt(userId),mobile_number,userOid,my_referral_code,token,shortToken)
//
//
//
//                    db.authDao().addData(auth)
//                    println(db.authDao().getAll()[0].user_id)
//                    val myIntent = Intent(activity, VinalifeActivity::class.java)
//                    startActivity(myIntent)

                }

            }

            override fun onFailure(call: Call<BaseResponseObject>, t: Throwable) {
                Toast.makeText(activity,t.toString(), Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun logout(){
        val db = Room.databaseBuilder(
            requireContext(),
            AuthDatabase::class.java, "auth"
        ).allowMainThreadQueries().build()
        db.authDao().deleteAll()
        val myIntent = Intent(requireContext(), AuthActivity::class.java)
        //val myIntent = Intent(this, DemoActivity::class.java)
        startActivity(myIntent)
    }

    private fun parseInfo(user:JSONObject,view: View){
        if(user.isNull("fullname")){
            Toast.makeText(context,"Looix kia user",Toast.LENGTH_LONG).show()
            return
        }
        view.account_fullname.text = user.get("fullname").toString()
        view.account_mobile.text = user.get("mobile").toString()
        if(!(user.get("avatar").toString().trim('"')).isNullOrEmpty()){
            Glide.with(view).load(user.get("avatar").toString().trim('"')).into(account_avatar)
        }

    }

    private fun parseList(list:MutableList<String>, view: View){

        val countSize = list.size

        for(i in 0..countSize-1){
//            val data  = list[i]
//            println(data)
            //val list = data.get("list")
           // println(list[i].get("label"))

            val gson = Gson().toJsonTree(list[i]).asJsonObject
            val label = gson.get("label").toString().trim('"')
            val dataList = gson?.get("list").toString()

            val jsonArray = JSONTokener(dataList).nextValue() as JSONArray

            var listItem:MutableList<AccountListInOther> = arrayListOf()
            for (j in 0 until jsonArray.length()) {
                // ID

                    //val type_rediect:String,
                //    val rediect_to:String
                val labelList = jsonArray.getJSONObject(j).getString("label").toString().trim('"')
                var contentList = (jsonArray.getJSONObject(j).getString("content").toString().trim('"'))
                if(contentList.toDoubleOrNull()!=null) contentList = Math.round(contentList.toDouble()).toString()
//                println(Math.round(contentList.toDouble()))
//                if(contentList!="" ) contentList = contentList.trimEnd('0').trimEnd('.')

                val typeRedirect = jsonArray.getJSONObject(j).getString("type_rediect").toString().trim('"')
                val redirectTo = jsonArray.getJSONObject(j).getString("rediect_to").toString().trim('"')


                listItem.add(AccountListInOther(labelList,contentList,typeRedirect,redirectTo))


                println(labelList)
//                Log.i("ID: ", id)
//
//                // Employee Name
//                val employeeName = jsonArray.getJSONObject(i).getString("employee_name")
//                Log.i("Employee Name: ", employeeName)
//
//                // Employee Salary
//                val employeeSalary = jsonArray.getJSONObject(i).getString("employee_salary")
//                Log.i("Employee Salary: ", employeeSalary)
//
//                // Employee Age
//                val employeeAge = jsonArray.getJSONObject(i).getString("employee_age")
//                Log.i("Employee Age: ", employeeAge)

                // Save data using your Model

                // Notify the adapter
            }

            //objectData.add(AccountOther(label.toString(),))
            //println(dataList);

           // IntStream.range(list, list.length)
           // val dataArray = Json.pa
//            for(j in 0..jsonA.size){
//
//            }
//
//            println(list.length)
            println("----"+dataList.javaClass.kotlin.simpleName)
            objectData.add(AccountOther(label,listItem))
        }
println("<--->"+objectData)


        accountOtherAdapter = AccountOtherAdapter(vinalifeActivity,view.context)
        account_other.apply {
            val linearLayoutManager = LinearLayoutManager(requireContext())
            layoutManager = linearLayoutManager
            adapter = accountOtherAdapter


        }.run {
            accountOtherAdapter.setData(objectData)
        }
    }

    fun isNumber(s: String?): Boolean {
        return if (s.isNullOrEmpty()) false else s.all { Character.isDigit(it) }
    }

    override fun openWebView(url: String) {
        //vinalifeActivity.openWebView(url)
    }
}
