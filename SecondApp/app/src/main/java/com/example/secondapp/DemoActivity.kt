package com.example.secondapp

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.secondapp.adapters.AccountOtherAdapter
import com.example.secondapp.model.User
import com.example.secondapp.models.AccountListInOther
import com.example.secondapp.models.AccountOther
import kotlinx.android.synthetic.main.activity_demo.*
import java.util.jar.Manifest

class DemoActivity : AppCompatActivity() {
    lateinit var accountOtherAdapter: AccountOtherAdapter

    private val cameraRequestId = 1222

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)

        permission()


        open_camera.setOnClickListener {
            permission()
            if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED){
                val cameraInt = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraInt,cameraRequestId)
            }else{
                Toast.makeText(applicationContext,"Vui lòng cho phép camera Hoạt động",Toast.LENGTH_LONG).show()
            }

        }




//        var objectData:ArrayList<AccountOther> = arrayListOf(
//            AccountOther("category1", arrayListOf(
//                AccountListInOther("category1.1","content 1","webview","https://facebook.com/me"),
//                AccountListInOther("category1.2","content 2","webview","https://facebook.com/me"),
//                AccountListInOther("category1.3","content 3","webview","https://facebook.com/me"),
//                AccountListInOther("category1.4","content 4","webview","https://facebook.com/me"),
//                AccountListInOther("category1.5","content 5","webview","https://facebook.com/me"),
//            )),
//            AccountOther("category2", arrayListOf(
//                AccountListInOther("category2.1","content 1","webview","https://facebook.com/me"),
//                AccountListInOther("category2.2","content 2","webview","https://facebook.com/me"),
//                AccountListInOther("category2.3","content 3","webview","https://facebook.com/me"),
//                AccountListInOther("category2.4","content 4","webview","https://facebook.com/me"),
//                AccountListInOther("category2.5","content 5","webview","https://facebook.com/me"),
//            )),
//            AccountOther("category3", arrayListOf(
//                AccountListInOther("category3.1","content 1","webview","https://facebook.com/me"),
//                AccountListInOther("category3.2","content 2","webview","https://facebook.com/me"),
//                AccountListInOther("category3.3","content 3","webview","https://facebook.com/me"),
//                AccountListInOther("category3.4","content 4","webview","https://facebook.com/me"),
//                AccountListInOther("category3.5","content 5","webview","https://facebook.com/me"),
//            )),
//        )

//        accountOtherAdapter = AccountOtherAdapter(this)
//
//
//        demo_recyclerview.apply{
//            val linearLayoutManager = LinearLayoutManager(context)
//            layoutManager = linearLayoutManager
//            adapter = accountOtherAdapter
//        }.run {
//            accountOtherAdapter.setData(objectData)
//        }

       // println(objectData)



       // setupActionBarWithNavController(findNavController(R.id.fragmentContainerView))
    }

    private fun permission(){
        if(ContextCompat.checkSelfPermission(
                applicationContext,android.Manifest.permission.CAMERA
            )==PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(
                this, arrayOf(android.Manifest.permission.CAMERA),cameraRequestId
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode===cameraRequestId){
            val images:Bitmap = data?.extras?.get("data") as Bitmap
            layout_camera.setImageBitmap(images)
        }
    }

//    override fun onSupportNavigateUp(): Boolean {
//        val navController = findNavController(R.id.fragmentContainerView)
//        return navController.navigateUp() ||  super.onSupportNavigateUp()
//    }
}