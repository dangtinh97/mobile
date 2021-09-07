package com.example.smartplug

import android.content.ContentValues.TAG
import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import java.net.URISyntaxException
import java.util.*


private const val URL="https://node-bui-van-viet.herokuapp.com/"
class SocketHandler(private var room:String="") {
    private var mSocket:Socket ?=null
    @Synchronized
    fun setSocket(){
        try{
            val opts = IO.Options()
            opts.transports = arrayOf("websocket","polling")
            opts.query = "send_from=mobile&key=$room"
            mSocket = IO.socket(URL,opts)
            println("success")
        }catch (e:Exception){
            e.printStackTrace()

            Log.d("fail", "Failed to connect")
        }
    }

    @Synchronized
    fun establishConnection() {
        mSocket?.connect()
    }

    @Synchronized
    fun getSocket(): Socket? {
        return mSocket
    }


}
