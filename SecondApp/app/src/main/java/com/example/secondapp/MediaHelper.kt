package com.example.secondapp

import android.graphics.Bitmap
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.annotation.RequiresApi
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*

class MediaHelper {
    var namaFile=""
    var fileUri = Uri.parse("")
    fun getOutPutMediaFile():File {
        val mediaStorageDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),"appx06")
        if(!mediaStorageDir.exists()){
            if(!mediaStorageDir.mkdirs()){
                Log.e("mkdirs","Vao day lam gi tao khong biet")
            }
        }
        val mediaFile = File(mediaStorageDir.path+File.separator+"${this.namaFile}")
        return mediaFile
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getOutPutMediaFIleUri(): Uri {
        val timestamp = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date())
        this.namaFile = "DC_${timestamp}.jpg"
        this.fileUri = Uri.fromFile(getOutPutMediaFile())
        return this.fileUri
    }

    fun bitmapToString(bmp:Bitmap):String{
        val outPutStream = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.JPEG,60,outPutStream)
        val byteArray:ByteArray = outPutStream.toByteArray()
        return ""
//        return Base64.encode

    }
}