package com.example.secondapp.fragments.notification

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.secondapp.AppConfig
import com.example.secondapp.R
import com.example.secondapp.VinalifeActivity
import com.example.secondapp.adapters.ListNotificationAdapter
import com.example.secondapp.data.BaseResponseObject
import com.example.secondapp.data.auth.AuthClient
import com.example.secondapp.databases.AuthDatabase
import com.example.secondapp.models.Notification
import kotlinx.android.synthetic.main.fragment_list_notification.*
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ListNotificationFragment(private val vinalifeActivity: VinalifeActivity) : Fragment() {

    private val client: AuthClient = AppConfig.retrofit.create(AuthClient::class.java)
    private var buttonFlag:Boolean = false
    lateinit var listNotificationAdapter: ListNotificationAdapter
    private var dyScroll:Int=0
    private var onLoading:Boolean = true
    private var lastOid:String = ""
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list_notification, container, false)
        val scv = view.findViewById<RecyclerView>(R.id.notification_items)
        scv.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if(newState==0 && dyScroll>1 && lastOid.isNotEmpty()){
                    onLoading = false

                    //Toast.makeText(context,lastOid,Toast.LENGTH_LONG).show()
                    fetchData(view,lastOid)
                }

                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                dyScroll = dy
            }
        })


        fetchData(view,"")
        return view
    }

    private fun fetchData(view: View,lastOid:String){
        this.lastOid = ""
        if(onLoading){
                (activity as VinalifeActivity).onLoading()
        }
        val db = Room.databaseBuilder(
            requireContext(),
            AuthDatabase::class.java, "auth"
        ).allowMainThreadQueries().build()
        db.authDao().getAll()[0].token

        val authorization:String = db.authDao().getAll()[0].token
        println("bearer ${authorization.trim('"')}")
        val service: Call<BaseResponseObject> = client.notification("bearer ${authorization.trim('"')}",lastOid)

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
//                    val user = data?.get("user").toString()
//                    val gift = data?.get("gift").toString()
//                    val packages = data?.get("packages").toString()
                    val listString = data?.get("list").toString()

                    if(listString!=""){
                        val list = JSONTokener(listString).nextValue() as JSONArray
                        parseListNotification(view,list)
                    }
//                    println(packages.javaClass.kotlin.simpleName)

//                    val others = data?.get("other").toString()
                    println(data)
//                    if (user != null) {
//                        val jsonObject = JSONTokener(user).nextValue() as JSONObject
//                        parseInfo(jsonObject,view)
//                    }
//
//                    if(gift != null){
//                        val jsonGift = JSONTokener(gift).nextValue() as JSONObject
//                        setOnclickGift(view,jsonGift)
//                    }
//
//                    if(packages!=""){
//                        val jsonPackage = JSONTokener(packages).nextValue() as JSONArray
//                        parsePackage(jsonPackage,view)
//                        println(jsonPackage)
//                    }
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

    private fun parseListNotification(view:View, items:JSONArray){
        var listNotification = arrayListOf<Notification>()
        for (i in 0..items.length()-1){
            val item = items[i].toString()
            val itemObject = JSONTokener(item).nextValue() as JSONObject
            val title = itemObject.get("title").toString().trim('"')
            val content = itemObject.get("content").toString().trim('"')
            val inbox_oid = itemObject.get("inbox_oid").toString().trim('"')
            this.lastOid = inbox_oid
            val type = itemObject.get("type").toString().trim('"')
            val time = itemObject.get("time").toString().trim('"')
            listNotification.add(Notification(inbox_oid,title,content,time,type))
        }


        if(!onLoading){
            val positionOld:Int = listNotificationAdapter.itemCount
            println("positionOLD"+positionOld)
            for(i in 0..listNotification.size - 1){
                listNotificationAdapter.addData(listNotification[i])
            }

            println("positionNEW"+listNotificationAdapter.itemCount)

//            notification_items.smoothScrollToPosition(positionOld-1)
            notification_items.scrollToPosition(positionOld)
//            listNotificationAdapter.registerAdapterDataObserver(object: RecyclerView.AdapterDataObserver(){
//            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
//                println("Toast 2"+listNotificationAdapter.itemCount)
//                notification_items.smoothScrollToPosition(listNotificationAdapter.itemCount +1)
//            }
//        })
        }else{
            listNotificationAdapter = ListNotificationAdapter(vinalifeActivity)
            notification_items.apply{
                val linearLayout = LinearLayoutManager(context)
                layoutManager = linearLayout
                adapter = listNotificationAdapter
            }.run {
                listNotificationAdapter.setData(listNotification)
            }
        }
    }

}