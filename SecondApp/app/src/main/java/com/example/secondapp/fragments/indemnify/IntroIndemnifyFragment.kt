package com.example.secondapp.fragments.indemnify

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.example.secondapp.AppConfig
import com.example.secondapp.R
import com.example.secondapp.VinalifeActivity
import com.example.secondapp.data.auth.AuthClient
import com.example.secondapp.data_local.SharedPrefs
import kotlinx.android.synthetic.main.fragment_into_indemnify.view.*
import kotlinx.android.synthetic.main.item_package.view.*

class IntroIndemnifyFragment(private val vinalifeActivity: VinalifeActivity) : Fragment() {

    lateinit var indemnifyFragment: IndemnifyFragment



    var onClick = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_into_indemnify, container, false)
        view.intro_text.setText(Html.fromHtml((SharedPrefs.instance.getObject("indemnify_setup").get("intro").toString()).replace("\n","<br>")))
       // Glide.with(view).load(SharedPrefs.instance.getObject("indemnify_setup").get("image").toString()).into(view.intro_image)

indemnifyFragment = IndemnifyFragment(vinalifeActivity)
        var bundle = Bundle()

        onClick = false
        val transaction:FragmentTransaction = fragmentManager!!.beginTransaction()
        view.required_me.setOnClickListener {
            if(onClick) return@setOnClickListener
            onClick = true
            bundle.putString("type_request","ME")
            indemnifyFragment.setArguments(bundle)

            transaction.replace(R.id.fragment_vinalife,indemnifyFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        view.required_other.setOnClickListener {
            if(onClick) return@setOnClickListener
            onClick = true
            bundle.putString("type_request", "OTHER")
            indemnifyFragment.setArguments(bundle)
            transaction.replace(R.id.fragment_vinalife, indemnifyFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        return view
    }
}