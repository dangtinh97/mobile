package com.example.radiofm.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.radiofm.R
import com.example.radiofm.fragments.RadioFragment
import com.example.radiofm.models.RadioModel
import kotlinx.android.synthetic.main.radio_item.view.*

class RadioAdapter(val fragment: RadioFragment, val radios:MutableList<RadioModel>) :RecyclerView.Adapter<RadioAdapter.ViewHolder>() {
    var idIsClick = ""
    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){

        fun bind(data:RadioModel){
            itemView.tv_name_radio.text = data.name
            if(idIsClick!=data.id)itemView.status_radio.visibility = View.GONE
            if(idIsClick==data.id)itemView.status_radio.visibility = View.VISIBLE

            itemView.item_radio_channel.setOnClickListener {
                itemView.status_radio.visibility = View.VISIBLE
                idIsClick = data.id

                fragment.fetchDataRadioDetail(data.id)
            }



//            if(data.id==idIsClick){
//                itemView.status_radio.visibility = View.VISIBLE
//                //itemView.status_radio.setTextColor(ContextCompat.getColor(context,R.color.success))
//
//            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RadioAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.radio_item,parent,false)
        val viewHolder = ViewHolder(view)
        return viewHolder
    }


    fun setIdPlayer(id:String){
        idIsClick = ""
        notifyDataSetChanged()
        idIsClick =id
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RadioAdapter.ViewHolder, position: Int) {
        holder.bind(radios[position])
    }

    override fun getItemCount(): Int {
        return radios.size
    }
}
