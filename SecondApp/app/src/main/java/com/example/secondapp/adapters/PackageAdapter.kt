package com.example.secondapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.secondapp.R
import com.example.secondapp.VinalifeActivity
import com.example.secondapp.models.Package
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.item_package.view.*

class PackageAdapter (private val vinalifeActivity: VinalifeActivity, private val items:ArrayList<com.example.secondapp.models.Package>) : RecyclerView.Adapter<PackageAdapter.ViewHolder>() {

    inner class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        fun bind(item: Package) {
            itemView.home_package_name_short.text = item.short_name
            itemView.home_package_money_text.text = item.money_text
            Glide.with(itemView).load(item.image_url).into(itemView.package_item_url_image)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PackageAdapter.ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_package,parent,false)
        val viewHolder = ViewHolder(view)

        view.package_item.setOnClickListener {
            val position = viewHolder.layoutPosition
            println(position)
            if(items[position].type_redirect=="webview"){
                Toast.makeText(parent.context,items[position].redirect_to,Toast.LENGTH_LONG).show()
                vinalifeActivity.openWebView(items[position].redirect_to)
            }
        }


        return viewHolder
    }

    override fun onBindViewHolder(holder: PackageAdapter.ViewHolder, position: Int) {

        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}