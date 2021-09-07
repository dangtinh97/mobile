package com.example.smartplug

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartplug.adapters.PlugAdapter
import com.example.smartplug.adapters.RoomAdapter
import com.example.smartplug.models.Plug
import com.example.smartplug.models.Room
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

     lateinit var roomAdapter:RoomAdapter
     lateinit var plugAdapter: PlugAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        println("Internet status")
        val online = isOnline(this)
        println("Internet status$online")


        val rooms:MutableList<Room> = arrayListOf()
        rooms.add(Room("Yêu thích",1))
//        rooms.add(Room("Phòng khách 2",2))
//        rooms.add(Room("Phòng khách 4",3))
//        rooms.add(Room("Phòng khách 3" ,4))
        roomAdapter = RoomAdapter(rooms)
        roomRecyclerView.apply {
            val linearLayoutManager =LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false)
            layoutManager = linearLayoutManager
            adapter = roomAdapter
        }
//        rooms.add(Room("Phòng khách 5" ,4))
        roomAdapter.setData(rooms)
        deviceInRoom()
    }

    private fun deviceInRoom(){
        val plugs:MutableList<Plug> = arrayListOf()

        plugs.add(Plug(1,"Ổ cắm phòng khách",1,"27356c30-d05a-4ec9-93ea-ea39efed4421"))

        plugAdapter = PlugAdapter(this,plugs)
        plugRecyclerView.apply {
            val linearLayoutManager =LinearLayoutManager(context)
            layoutManager = linearLayoutManager
            adapter = plugAdapter
        }
    }

    public fun clickPlug(data:Plug){
        val mIntent = Intent(this@MainActivity,PlugActivity::class.java)
        mIntent.putExtra("room_key",data.room)
        mIntent.putExtra("room_name",data.name)
        startActivity(mIntent)
    }

    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        Log.i("Internet", "NO INTERNET")
        return false
    }
}
