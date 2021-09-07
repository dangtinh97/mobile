package com.example.secondapp

import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.example.secondapp.databases.AuthDatabase
import kotlinx.android.synthetic.main.activity_auth.*

class WebviewActivity : AppCompatActivity() {


    val CAMERA_RQ = 102
    val FILE_CHOOSER_RESULT_CODE = 10000

    private var uploadMessage: ValueCallback<Uri>? = null
    private var uploadMessageAboveL: ValueCallback<Array<Uri>>? = null

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)
        checkForPermission(android.Manifest.permission.CAMERA,"camera",CAMERA_RQ)
        // calling the action bar
        onLoading()
        val actionBar = getSupportActionBar()

//
//        // showing the back button in action bar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setDisplayShowHomeEnabled(true)
            actionBar.title = "Vinalife"
        }



        val db = Room.databaseBuilder(
            this,
            AuthDatabase::class.java, "auth"
        ).allowMainThreadQueries().build()

        val data = db.authDao().getAll()

        var url=intent.getStringExtra("url").toString()

//        url = "https://dev.vinalife.vn/huong-dan-thanh-toan-bao-hiem-online"

        var firstParam = "?"
        if(url.indexOf("?") > 2){
            println("------vào đây "+url.indexOf("?"))
            firstParam = "&"
        }

        var addUrl = firstParam+"user_id=${data[0].user_id}&user_oid=${data[0].user_oid}&short_token=${data[0].short_token}"
        url = url+addUrl

        webView = findViewById(R.id.webview)
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.settings.setJavaScriptEnabled(true)
        println("------"+url)

        //url="https://docs.google.com/gview?embedded=true&url=https://storage.googleapis.com/lian-test-bucket/20210628/certificate072020/_60338c56a45d36c1778b4568.pdf"

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url != null) {
                    onLoading()
                    println("------NEXT___"+url)
                    view?.loadUrl(url)
                }
                return true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                closeLoading()
                actionBar?.title = view?.title
                //super.onPageFinished(view, url)
            }
        }
        webView.webChromeClient = object : WebChromeClient(){
            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {
                uploadMessageAboveL = filePathCallback;
                
                openImageChooserActivity()
                return true;
            }
        }

        webView.loadUrl(url)
        println("---"+url)
    }


    private fun openImageChooserActivity() {
        val i = Intent(Intent.ACTION_GET_CONTENT)
        i.addCategory(Intent.CATEGORY_OPENABLE)

        i.type = "image/*"
        startActivityForResult(Intent.createChooser(i, "Image Chooser"), FILE_CHOOSER_RESULT_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FILE_CHOOSER_RESULT_CODE) {
            if (null == uploadMessage && null == uploadMessageAboveL) return
            val result = if (data == null || resultCode != Activity.RESULT_OK) null else data.data
            if (uploadMessageAboveL != null) {
                onActivityResultAboveL(requestCode, resultCode, data)
            } else if (uploadMessage != null) {
                uploadMessage!!.onReceiveValue(result)
                uploadMessage = null
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val cookieSyncManager:CookieSyncManager = CookieSyncManager.createInstance(applicationContext)
        val cookieManager:CookieManager = CookieManager.getInstance()
        cookieManager.removeAllCookie()
        onBackPressed()
        return true
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun onActivityResultAboveL(requestCode: Int, resultCode: Int, intent: Intent?) {
        if (requestCode != FILE_CHOOSER_RESULT_CODE || uploadMessageAboveL == null)
            return
        var results: Array<Uri>? = null
        if (resultCode == Activity.RESULT_OK) {
            if (intent != null) {
                val dataString = intent.dataString
                val clipData = intent.clipData
//                if (clipData != null) {
//                    results = arrayOfNulls(clipData.itemCount)
//                    for (i in 0 until clipData.itemCount) {
//                        val item = clipData.getItemAt(i)
//                        results[i] = item.uri
//                    }
//                }
                if (dataString != null)
                    results = arrayOf(Uri.parse(dataString))
            }
        }
        uploadMessageAboveL!!.onReceiveValue(results)
        uploadMessageAboveL = null
    }

    private fun onLoading(){
        loading_relative.visibility = View.VISIBLE
        loading_image.animate().apply {
            duration = 10000
            rotation(3600f)
        }.start()
    }

    private fun closeLoading(){
        loading_image.animate().apply {
            duration = 10000
            rotation(3600f)
        }.cancel()
        loading_relative.visibility = View.GONE
    }

    private fun checkForPermission(permission:String, name:String,requestCode:Int){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            when{
                ContextCompat.checkSelfPermission(applicationContext,permission)== PackageManager.PERMISSION_GRANTED->{
                    Toast.makeText(applicationContext,"$name permission granted",Toast.LENGTH_LONG).show()
                }
                shouldShowRequestPermissionRationale(permission)->showDialog(permission,name,requestCode)
                else->ActivityCompat.requestPermissions(this, arrayOf(permission),requestCode)
            }
        }
    }

    /** trở lại webview */

//    override fun onBackPressed() {
//        if(webView.canGoBack()){
//            webView.goBack()
//            Toast.makeText(applicationContext,"CAN BACK",Toast.LENGTH_LONG).show()
//        }else{
//            Toast.makeText(applicationContext,"CAN NOT BACK",Toast.LENGTH_LONG).show()
//            super.onBackPressed()
//        }
//
//    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        fun innerCheck(name: String){
            if(grantResults.isEmpty() || grantResults[0]!=PackageManager.PERMISSION_GRANTED){
                Toast.makeText(applicationContext,"$name permission refused",Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(applicationContext,"$name permission granted",Toast.LENGTH_LONG).show()
            }
            when(requestCode){
                CAMERA_RQ->innerCheck("camera")
            }
        }

        //super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun showDialog(permission:String, name:String,requestCode:Int){
        val builder = AlertDialog.Builder(this)
        builder.apply {
            setMessage("Permission to access your $name is required to use this app")
            setTitle("Permission required")
            setPositiveButton("OKE"){
                dialog,which->ActivityCompat.requestPermissions(this@WebviewActivity,
                arrayOf(permission),requestCode)
            }
        }
    }

}