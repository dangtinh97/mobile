package com.example.secondapp.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.secondapp.AuthActivity
import com.example.secondapp.R
import com.example.secondapp.VinalifeActivity
import com.example.secondapp.interfaces.LoadingInterface
import com.example.secondapp.models.AccountListInOther
import kotlinx.android.synthetic.main.item_other_account.view.*

class AccountItemOtherAdapter (private val vinalifeActivity:VinalifeActivity,private var context: Context, private var items:List<AccountListInOther>) : RecyclerView.Adapter<AccountItemOtherAdapter.ViewHolder> ()  {



    inner class ViewHolder(itemView:View) :  RecyclerView.ViewHolder(itemView){
        fun bind(accountListInOther: AccountListInOther) {
            itemView.tv_label_item_other.text = accountListInOther.label
            itemView.tv_content_item_other.text = accountListInOther.content
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = (LayoutInflater.from(context).inflate(R.layout.item_other_account,parent,false))
        val viewHolder = ViewHolder(view)

        view.relative_layout_account_user_other_item.setOnClickListener {
            val position = viewHolder.layoutPosition;
            println(position)
            val valuePosition:AccountListInOther = (items[position])
            val typeRedirect = valuePosition.type_rediect
            println(valuePosition.rediect_to)
            if(typeRedirect=="webview"){
                vinalifeActivity.openWebView(valuePosition.rediect_to)
            }
            Toast.makeText(context,"type redirect ${valuePosition.type_rediect}",Toast.LENGTH_LONG).show()

        }
        return viewHolder
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }


    override fun getItemCount(): Int {
        return items.size
    }
}


