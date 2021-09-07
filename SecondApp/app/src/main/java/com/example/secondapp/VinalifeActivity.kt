package com.example.secondapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.secondapp.fragments.home.HomeFragment
import com.example.secondapp.fragments.indemnify.IntroIndemnifyFragment
import com.example.secondapp.fragments.notification.ListNotificationFragment
import com.example.secondapp.fragments.user.AccountFragment
import com.example.secondapp.interfaces.LoadingInterface
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.activity_auth.loading_image
import kotlinx.android.synthetic.main.activity_auth.loading_relative
import kotlinx.android.synthetic.main.activity_vinalife.*

class VinalifeActivity(override val loading_image: Any, override val loading_relative: Any) : AppCompatActivity(),LoadingInterface {

    lateinit var accountFragment:AccountFragment
    lateinit var homeFragment: HomeFragment
    lateinit var listNotificationFragment: ListNotificationFragment
    lateinit var introIndemnifyFragment: IntroIndemnifyFragment
    var itemIdLoadClick:Boolean =false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vinalife)
//        fragmentProfile(this)
        setHomeFragment(this)
//        setNotifyFragment(this)
        //setIndeminifyFragment(this)
                bottom_navigation.setOnNavigationItemSelectedListener { item ->
                    if (itemIdLoadClick) {
                        true
                    } else {
                        when (item.itemId) {
                            R.id.action_home -> {
                                setHomeFragment(this)
                                true
                            }
                            R.id.action_indemnify->{
                                setIndeminifyFragment(this)
                                true
                            }
                            R.id.action_account -> {
                                fragmentProfile(this)
                                true
                            }
                            R.id.action_notify -> {
                                setNotifyFragment(this)
                                true
                            }
                        }
                        true

                    }
                }

    }

    private fun setNotifyFragment(activity: VinalifeActivity){
        listNotificationFragment = ListNotificationFragment(activity)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_vinalife,listNotificationFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun setIndeminifyFragment(activity: VinalifeActivity){
        introIndemnifyFragment = IntroIndemnifyFragment(activity)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_vinalife,introIndemnifyFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun fragmentProfile (activity:VinalifeActivity){

        accountFragment = AccountFragment(activity)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_vinalife,accountFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun setHomeFragment(activity:VinalifeActivity){
        homeFragment = HomeFragment(activity)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_vinalife,homeFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    internal fun onLoading(){
        itemIdLoadClick = true
        loading_relative.visibility = View.VISIBLE
        loading_image.animate().apply {
            duration = 10000
            rotation(3600f)
        }.start()
    }

    internal fun closeLoading(){
        itemIdLoadClick =false
        loading_image.animate().apply {
            duration = 10000
            rotation(3600f)
        }.cancel()
        loading_relative.visibility = View.GONE
    }

    override fun openWebView(url:String) {
        val intent = Intent(this, WebviewActivity::class.java)
        intent.putExtra("url", url)
        startActivity(intent)
    }
}
