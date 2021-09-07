package com.example.reviewphim

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings.Secure
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.reviewphim.services.ApiService
import com.example.reviewphim.services.SharedPreferencesService
import java.lang.reflect.Method
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*

class SplashActivity : AppCompatActivity() {

    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val android_id = Secure.getString(
            this.getContentResolver(),
            Secure.ANDROID_ID
        )

        val infoApp = getDeviceSuperInfo();

        ApiService().installAppFirst(android_id,infoApp)

        Handler().postDelayed({
            val intent = Intent(this@SplashActivity,MainActivity::class.java)
            startActivity(intent)
            finish()
        },800)
    }

    private fun getDeviceSuperInfo(): String {

        var s = "Debug-infos:"
        try {
            s += """
 OS Version: ${System.getProperty("os.version")}(${Build.VERSION.INCREMENTAL})"""
            s += """
 OS API Level: ${Build.VERSION.SDK_INT}"""
            s += """
 Device: ${Build.DEVICE}"""
            s += """
 Model (and Product): ${Build.MODEL} (${Build.PRODUCT})"""
            s += """
 RELEASE: ${Build.VERSION.RELEASE}"""
            s += """
 BRAND: ${Build.BRAND}"""
            s += """
 DISPLAY: ${Build.DISPLAY}"""
            s += """
 CPU_ABI: ${Build.CPU_ABI}"""
            s += """
 CPU_ABI2: ${Build.CPU_ABI2}"""
            s += """
 UNKNOWN: ${Build.UNKNOWN}"""
            s += """
 HARDWARE: ${Build.HARDWARE}"""
            s += """
 Build ID: ${Build.ID}"""
            s += """
 MANUFACTURER: ${Build.MANUFACTURER}"""
            s += """
 SERIAL: ${Build.SERIAL}"""
            s += """
 USER: ${Build.USER}"""
            s += """
 HOST: ${Build.HOST}"""
            Log.e(TAG, "PHONE:INFO$s")
        } catch (e: Exception) {
            Log.e(TAG, "Error getting Device INFO")
        }
        return s;
    }

    fun md5(input:String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }
}
