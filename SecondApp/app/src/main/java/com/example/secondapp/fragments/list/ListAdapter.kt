package com.example.secondapp.fragments.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.secondapp.R
import com.example.secondapp.data.User
import kotlinx.android.synthetic.main.custome_row.view.*

class ListAdapter : RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    private var userList = emptyList<User>()

    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custome_row,parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //để gán giá trị
         val currentItem = userList[position]

        holder.itemView.tv_stt.text = currentItem.id.toString()
        holder.itemView.tv_first_name.text = currentItem.first_name.toString()
    }

    fun setData(user: List<User>){
        this.userList = user
        notifyDataSetChanged()

    }

    override fun getItemCount(): Int {
       return userList.size
    }
}