package com.example.secondapp.fragments.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.room.Room
import com.example.secondapp.*
import com.example.secondapp.adapters.PackageAdapter
import com.example.secondapp.data.BaseResponseObject
import com.example.secondapp.data.auth.AuthClient
import com.example.secondapp.databases.AuthDatabase
import com.example.secondapp.models.Package
import kotlinx.android.synthetic.main.fragment_home_vinalife.*
import kotlinx.android.synthetic.main.fragment_home_vinalife.view.*
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment(private val vinalifeActivity: VinalifeActivity) : Fragment() {

    private val client: AuthClient = AppConfig.retrofit.create(AuthClient::class.java)
    private var buttonFlag:Boolean = false
    lateinit var user: JSONObject
    lateinit var packageAdapter: PackageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home_vinalife, container, false)
        fetchData(view)

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
        val service: Call<BaseResponseObject> = client.home("bearer ${authorization.trim('"')}")

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
                    val gift = data?.get("gift").toString()
                    val packages = data?.get("packages").toString()

                    println(packages.javaClass.kotlin.simpleName)

//                    val others = data?.get("other").toString()
                    println(data)
                    if (user != null) {
                        val jsonObject = JSONTokener(user).nextValue() as JSONObject
                        parseInfo(jsonObject,view)
                    }

                    if(gift != null){
                        val jsonGift = JSONTokener(gift).nextValue() as JSONObject
                        setOnclickGift(view,jsonGift)
                    }

                    if(packages!=""){
                        val jsonPackage = JSONTokener(packages).nextValue() as JSONArray
                        parsePackage(jsonPackage,view)
                        println(jsonPackage)
                    }
//
//                    if(others!=null){
//                        val jsonArray = Gson().fromJson(others, mutableListOf<String>().javaClass)
//
//                        parseList(jsonArray,view)
//                        //println(jsonArray[0])
//
//                    }



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

    private fun setOnclickGift(view:View, gift:JSONObject){
        view.tv_home_gift.setOnClickListener {
            val myIntent = Intent(requireContext(), WebviewActivity::class.java)
            myIntent.putExtra("url",gift.get("redirect_to").toString().trim('"'))
            startActivity(myIntent)
        }
    }

    private fun parsePackage(packages:JSONArray, view: View){
        println(packages.length())
        var models = arrayListOf<com.example.secondapp.models.Package>()
        for(i in 0..packages.length()-1){
            val item = packages[i].toString()
            val jsonItem = JSONTokener(item).nextValue() as JSONObject
            val shortName = jsonItem.get("short_name").toString().trim('"')
            val urlImage = jsonItem.get("url_image").toString().trim('"')
            val moneyText = jsonItem.get("fees_from_text").toString().trim('"')
            val typeRedirect = jsonItem.get("type_redirect").toString().trim('"')
            val redirectTo = jsonItem.get("redirect_to").toString().trim()
            models.add(Package(shortName,moneyText,urlImage,typeRedirect,redirectTo))

        }
        packageAdapter = PackageAdapter(vinalifeActivity,models)



        package_recycler_view.apply{
            val girdLayout = GridLayoutManager(context,2)
            layoutManager = girdLayout
            adapter = packageAdapter
            setHasFixedSize(true)
            setItemViewCacheSize(20)
        }
    }


    private fun parseInfo(user:JSONObject,view: View){
        view.tv_home_name.text ="Xin chào, "+ user.get("fullname").toString()+"!"
        view.tv_home_money.text =user.get("money_text").toString()
//        view.account_mobile.text = user.get("mobile").toString()
//        if(user.get("avatar").toString()!==""){
//            Glide.with(view).load(user.get("url").toString().trim('"')).into(account_avatar)
//        }

    }
}