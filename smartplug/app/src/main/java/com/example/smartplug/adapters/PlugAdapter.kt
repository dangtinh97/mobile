package com.example.smartplug.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smartplug.MainActivity
import com.example.smartplug.R
import com.example.smartplug.models.Plug
import kotlinx.android.synthetic.main.plug_item.view.*

class PlugAdapter(private var context:MainActivity,private var plugs:MutableList<Plug>):RecyclerView.Adapter<PlugAdapter.ViewHolder>() {
    inner class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        fun bind(data:Plug){
            itemView.detail_plug.setOnClickListener {
                context.clickPlug(data)
            }

            itemView.plug_item_text.text = data.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.plug_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(plugs[position])
    }

    override fun getItemCount(): Int {
        return plugs.size
    }
}
