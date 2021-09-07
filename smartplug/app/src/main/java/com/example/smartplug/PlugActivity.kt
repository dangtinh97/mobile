package com.example.smartplug

import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartplug.adapters.DeviceAdapter
import com.example.smartplug.models.Device
import com.example.smartplug.models.SocketConnect
import com.google.gson.Gson
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_plug.*


private const val EVENT_CHANGE_STATUS = "change-status"
private const val EVENT_CONNECTION = "event-connection"
private const val DEVICE_PLUG = "plug"
class PlugActivity : AppCompatActivity() {
    private var mSocket: Socket?=null;
    private var tvStatusDevice:TextView ?= null
    private var deviceOnline:Boolean = false
    val gson: Gson = Gson()
    private lateinit var socketHandler:SocketHandler
    private lateinit var deviceAdapter: DeviceAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plug)
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        val key = intent.getStringExtra("room_key")
        //khai bao

        val devices:MutableList<Device> = arrayListOf()
        devices.add(Device("1",true,"Quạt"))
        devices.add(Device("2",true,"Đèn"))
        devices.add(Device("3",false,"Máy tính"))
        devices.add(Device("4",true,"Tivi"))
        deviceAdapter = DeviceAdapter(this,devices)
        tvStatusDevice = findViewById<TextView>(R.id.status_device)
        socketHandler = SocketHandler(key.toString())
        socketHandler.setSocket()
        socketHandler.establishConnection()
        mSocket = socketHandler.getSocket()
        //on socket
        mSocket?.on(EVENT_CONNECTION,onConnect)
        mSocket?.on("event-disconnect",disconnect)
        mSocket?.on(EVENT_CHANGE_STATUS,deviceChange)

        //end on socket
        val name = intent.getStringExtra("room_name")


        name_room.text = name
        back_home.setOnClickListener {
            onBackPressed()
        }



        device_recycle_view.apply {
            val linearLayoutManager = LinearLayoutManager(context)
            layoutManager = linearLayoutManager
            adapter = deviceAdapter
        }
    }

    fun clickChangeStatusDevice(device:Device){
        if(!deviceOnline) return Toast.makeText(this,"Chưa kết nối thiết bị",Toast.LENGTH_LONG).show()

        val objectData = gson.toJson(device);
        mSocket?.emit(EVENT_CHANGE_STATUS,objectData)
        println("===$objectData")
    }

    private var deviceChange = Emitter.Listener {

        val jsonObject = gson.fromJson(it[0].toString(),Device::class.java);
        println("===${jsonObject}")
        if(jsonObject.send_from != DEVICE_PLUG) return@Listener
        val port = jsonObject.port
        val status = jsonObject.status
        runOnUiThread {deviceAdapter.updateStatus(port,status)}
    }

   private var onConnect = Emitter.Listener {
        val jsonObject = gson.fromJson(it[0].toString(),SocketConnect::class.java);
        println("===${jsonObject.send_from}")
        if(jsonObject.send_from != DEVICE_PLUG) return@Listener
        runOnUiThread { tvStatusDevice?.setText("(Đã kết nối)") }
        deviceOnline = true
    }

    private var disconnect = Emitter.Listener {
        val jsonObject = gson.fromJson(it[0].toString(),SocketConnect::class.java);
        println("===d${jsonObject.send_from}")
        if(jsonObject.send_from != DEVICE_PLUG) return@Listener
        runOnUiThread { tvStatusDevice?.setText("(Chưa kết nối)") }
        deviceOnline = false
    }

}
