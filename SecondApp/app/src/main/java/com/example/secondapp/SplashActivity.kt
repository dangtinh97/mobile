package com.example.secondapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.secondapp.data.BaseResponseObject
import com.example.secondapp.data.auth.AuthClient
import com.example.secondapp.data_local.SharedPrefs
import com.example.secondapp.databases.AuthDatabase
import com.example.secondapp.databases.SetupAppDatabase
import com.example.secondapp.models.SetupApp
import com.example.secondapp.models.SetupAppIndemify
import com.google.gson.JsonObject
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class SplashActivity : AppCompatActivity() {

    private val client: AuthClient = AppConfig.retrofit.create(AuthClient::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash) //goi man hinh :D

//        val myIntent = Intent(this, DemoActivity::class.java)
//        startActivity(myIntent)
//        return

        val currentTime: Long = Calendar.getInstance().getTime().time

        loadSetupApp()

        val timeLoadApp: Long =100*( Calendar.getInstance().getTime().time - currentTime)
        var timeD = 0;

        if(3000 - timeLoadApp >0 ){
                timeD = (3000 - timeLoadApp).toInt()
            }

        Handler().postDelayed({
            if(!isLogin()){
                val myIntent = Intent(this, AuthActivity::class.java)
                //val myIntent = Intent(this, DemoActivity::class.java)
                startActivity(myIntent)
            }else{
                // val myIntent = Intent(this, AuthActivity::class.java)
                val myIntent = Intent(this, VinalifeActivity::class.java)
                startActivity(myIntent)
            }
        }, timeD.toLong())

    }

    private fun loadSetupApp(){
        val service: Call<BaseResponseObject> = client.setupApp()
        service.enqueue(object: Callback<BaseResponseObject> {
            override fun onResponse(call: Call<BaseResponseObject>, response: Response<BaseResponseObject>) {
                //buttonFlag = false
                if(response.isSuccessful){
                    //(activity as AuthActivity).closeLoading()
                    val body = response.body()
                    if(body?.status!=200){
                        //Toast.makeText(this,response.body()?.content, Toast.LENGTH_LONG).show()
                        return
                    }
                    saveData(response.body()?.getData())
//                    val data = body.data
//                    val userId = data?.get("user_id").toString()
//                    val mobile_number = data?.get("mobile").toString()
//                    val userOid = data?.get("user_oid").toString()
//                    val my_referral_code = data?.get("my_referral_code").toString()
//                    val token = data?.get("token").toString()
//                    val shortToken = data?.get("short_token").toString()
//                    val auth = Auth(0,Integer.parseInt(userId),mobile_number,userOid,my_referral_code,token,shortToken)
//
//                    val db = Room.databaseBuilder(
//                        requireContext(),
//                        AuthDatabase::class.java, "auth"
//                    ).allowMainThreadQueries().build()

//                    db.authDao().addData(auth)
//                    println(db.authDao().getAll()[0].user_id)
//                    val myIntent = Intent(activity, VinalifeActivity::class.java)
//                    startActivity(myIntent)

                }

            }

            override fun onFailure(call: Call<BaseResponseObject>, t: Throwable) {
               // Toast.makeText(requireContext(),t.toString(), Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun saveData(data: JsonObject?){

        val db = Room.databaseBuilder(
        this,
        SetupAppDatabase::class.java, "setup_app"
        ).allowMainThreadQueries().build()

        println(data?.javaClass?.kotlin?.simpleName)
        val list = mutableListOf<SetupApp>()
        db.setUpAppDao().deleteAll()

        val array = arrayOf("terms_and_conditions","wait_build_pdf","link_share","url_payment","hotline","loyalty_url","ideminify_intro")
        for (i in 0..array.size-1){
            val result = (data?.get(array[i])).toString().trim('"')
            list.add(SetupApp(0,array[i],result))
            db.setUpAppDao().addData(SetupApp(0,array[i],result))
        }

        val indemnifyIntroString = data?.get("ideminify_intro").toString()



       // val indemnifyIntro = JSONTokener(indemnifyIntroString).nextValue() as JSONObject

        //val mSetUpAppIndemnify = SetupAppIndemify(indemnifyIntro.get("image").toString().trim('"'),indemnifyIntro.get("intro").toString().trim('"'),indemnifyIntro.get("description").toString().trim('"'))
        //println("setup-app1"+mSetUpAppIndemnify)
        SharedPrefs.instance.put("indemnify_setup",indemnifyIntroString)

        println("setup-app"+SharedPrefs.instance.getObject("indemnify_setup").get("image"))

        println( db.setUpAppDao().getAll())
        //

    }

    private fun isLogin():Boolean{
        val db = Room.databaseBuilder(
            this,
            AuthDatabase::class.java, "auth"
        ).allowMainThreadQueries().build()

        val data = db.authDao().getAll()

        if(data.isEmpty()) return false
        return true

    }
}