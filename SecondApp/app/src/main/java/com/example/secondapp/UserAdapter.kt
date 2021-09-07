package com.example.secondapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.secondapp.model.User
import kotlinx.android.synthetic.main.user_adpter.view.*

class UserAdapter : RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    private var users : MutableList<User> = mutableListOf()

    inner class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        fun bind(user : User){
            itemView.tv_username.text = user.fullname
        }
    }

    fun setData(data : MutableList<User>){
        users = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var holder = LayoutInflater.from(parent.context).inflate(R.layout.user_adpter,parent,false)
        return ViewHolder(holder)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount(): Int {
        return users.size;
    }
}