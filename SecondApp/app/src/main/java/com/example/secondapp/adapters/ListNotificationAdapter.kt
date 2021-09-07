package com.example.secondapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.secondapp.R
import com.example.secondapp.VinalifeActivity
import com.example.secondapp.models.Notification
import kotlinx.android.synthetic.main.notification_item.view.*

class ListNotificationAdapter(private val vinalifeActivity: VinalifeActivity) : RecyclerView.Adapter<ListNotificationAdapter.MyViewHolder>() {

    private var items:MutableList<Notification> = mutableListOf()

    inner class MyViewHolder(view:View):RecyclerView.ViewHolder(view){
        fun bind(item:Notification){
            itemView.notification_title.text = item.title
            itemView.notification_content.text = item.content
            itemView.notification_time.text = item.time
        }
    }

    public fun setData(itemsData:MutableList<Notification>){
        items = itemsData
        notifyDataSetChanged()
    }

    fun addData(item: Notification){
        this.items.add(item)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListNotificationAdapter.MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.notification_item,parent,false))
    }

    override fun onBindViewHolder(holder: ListNotificationAdapter.MyViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

}