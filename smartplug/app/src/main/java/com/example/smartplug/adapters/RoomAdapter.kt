package com.example.smartplug.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartplug.R
import com.example.smartplug.models.Room
import kotlinx.android.synthetic.main.room_item.view.*

class RoomAdapter(private var dataSet:MutableList<Room>):RecyclerView.Adapter<RoomAdapter.ViewHolder>() {

    var nameClick:String = ""
    inner class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        //val textView: TextView
        @SuppressLint("ResourceAsColor")
        fun bind(data: Room){

            itemView.room_item.setTextColor(Color.parseColor("#8E8E8E"))
            //println("====render")
            //itemView.room_item.setTextColor(Color.parseColor("#FFFFFF"))
            itemView.room_item.setOnClickListener{
                nameClick = data.name
                notifyDataSetChanged()
            }
            println("====render"+nameClick)
            if(nameClick==data.name || (nameClick==""&& data.name==dataSet[0].name)) itemView.room_item.setTextColor(Color.parseColor("#FFFFFF"))
            itemView.room_item.text = data.name
        }
    }

    fun setData(dataSetNew:MutableList<Room>){
        dataSet = dataSetNew
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.room_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        //holder.textView.text = dataSet[position].name
        holder.bind(dataSet[position])
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

}
