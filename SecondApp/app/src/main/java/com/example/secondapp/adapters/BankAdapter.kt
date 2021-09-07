package com.example.secondapp.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import com.example.secondapp.R
import com.example.secondapp.model.Bank
import kotlinx.android.synthetic.main.item_dropdown.view.*
import kotlinx.android.synthetic.main.item_dropdown_selected.view.*

class BankAdapter(context: Context, resource: Int, objects: MutableList<Bank>) :
    ArrayAdapter<Bank>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val convertView:View = LayoutInflater.from(parent.context).inflate(R.layout.item_dropdown_selected,parent,false)
        val bank = this.getItem(position)
        if(bank!=null){
            convertView.tv_item_selected.text = bank.name
        }

        return convertView
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {

        val convertView:View = LayoutInflater.from(parent.context).inflate(R.layout.item_dropdown,parent,false)
        val bank = this.getItem(position)
        if(bank!=null){
            convertView.tv_item_select.text = bank.name
        }


//
//        convertView.setOnClickListener {
//            convertView.tv_item_select.setTextColor(ContextCompat.getColor(context, R.color.main_blue_focus))
//        }

        return convertView
        //return super.getDropDownView(position, convertView, parent)
    }
}