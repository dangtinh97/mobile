package com.example.secondapp.fragments.add

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.example.secondapp.R
import com.example.secondapp.data.User
import com.example.secondapp.data.UserDatabase
import com.example.secondapp.data.UserRepository
import com.example.secondapp.data.UserViewModel
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_add.view.*

class AddFragment : Fragment() {

    private lateinit var mUserViewModel:UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add, container, false)

        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        view.btn_add.setOnClickListener{
            insertDataToDataBase()
        }
        return view
    }

    private fun insertDataToDataBase() {
        val firstName = et_first_name.text.toString()
        val lastName = et_last_name.text.toString()
        val age = et_age.text
        if(inputCheck(firstName,lastName,age)){
            val user = User(0,firstName,lastName,Integer.parseInt(age.toString()))
            val db = Room.databaseBuilder(
                requireContext(),
                UserDatabase::class.java, "table_user"
            ).allowMainThreadQueries().build()

            db.userDao().addUser2(user)


//            mUserViewModel.deleteAll()
//            mUserViewModel.addUser(user)
            Toast.makeText(requireContext(),"Create User Success",Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        }else{
            Toast.makeText(requireContext(),"please fill out all fields",Toast.LENGTH_LONG).show()
        }
    }

    private fun inputCheck(firstName:String, lastName:String, age:Editable):Boolean{
        return !(TextUtils.isEmpty(firstName) && TextUtils.isEmpty(lastName) && age.isEmpty())
    }
}