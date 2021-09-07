package com.example.secondapp.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.secondapp.R
import com.example.secondapp.VinalifeActivity
import com.example.secondapp.models.AccountListInOther
import com.example.secondapp.models.AccountOther
import kotlinx.android.synthetic.main.other_account_adapter.view.*

class AccountOtherAdapter (private val vinalifeActivity:VinalifeActivity,private val context: Context) : RecyclerView.Adapter<AccountOtherAdapter.ViewHolder>() {

    private var otherAccount: MutableList<AccountOther> =  mutableListOf()
    //lateinit var other_adapter_item_r:RecyclerView
    lateinit var itemAdapter:AccountItemOtherAdapter
    inner class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        val other_adapter_item_r: RecyclerView

        init {
            other_adapter_item_r = itemView.findViewById(R.id.other_adapter_item)
        }

        fun bind(others: AccountOther){
            if(others.label==""){
                itemView.tv_label_other.visibility = View.GONE
            }else{
                itemView.tv_label_other.text = others.label
            }

        }
    }

    fun setData(data:MutableList<AccountOther>){
        otherAccount = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AccountOtherAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.other_account_adapter,parent,false))
    }

    override fun onBindViewHolder(holder: AccountOtherAdapter.ViewHolder, position: Int) {
         holder.bind(otherAccount[position])
        setItem(holder.other_adapter_item_r,otherAccount[position].list)
    }

    override fun getItemCount(): Int {
        return otherAccount.size
    }

    private fun setItem(recyclerView: RecyclerView,list:List<AccountListInOther>){

        itemAdapter = AccountItemOtherAdapter(vinalifeActivity,context,list)

        val linearLayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = itemAdapter

    }
}