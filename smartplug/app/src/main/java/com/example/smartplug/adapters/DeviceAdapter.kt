package com.example.smartplug.adapters
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


import androidx.recyclerview.widget.RecyclerView
import com.example.smartplug.PlugActivity
import com.example.smartplug.R
import com.example.smartplug.models.Device

import kotlinx.android.synthetic.main.device_item.view.*


class DeviceAdapter(private var context:PlugActivity,private var devices:MutableList<Device>):RecyclerView.Adapter<DeviceAdapter.ViewHolder>() {
    inner class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        fun bind(data:Device){

            if(data.status) itemView.device.setBackgroundResource( R.drawable.device_on)
            if(!data.status) itemView.device.setBackgroundResource( R.drawable.device_off)
            itemView.name_device.text = data.name
            itemView.switch_device.setOnClickListener {
                println("___"+data.status)
                data.status = !data.status
                context.clickChangeStatusDevice(data)
                notifyDataSetChanged()
            }
        }
    }

    fun updateStatus(port:String,status:Boolean){
        devices.forEach{
           if(it.port==port) it.status = status
        }

         notifyDataSetChanged()
        //notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.device_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(devices[position])
    }

    override fun getItemCount(): Int {
        return devices.size
    }

}
