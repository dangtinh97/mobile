package com.example.radiofm.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.radiofm.R
import com.example.radiofm.fragments.AccountFragment
import com.example.radiofm.models.TimerModel
import kotlinx.android.synthetic.main.timer_item.view.*

class TimerAdapter(private val accountFragment: AccountFragment):RecyclerView.Adapter<TimerAdapter.ViewHolder>() {

    private val listTime : MutableList<TimerModel> = arrayListOf()
    private var timer = 0;

    init {

        listTime.add(TimerModel("Không hẹn giờ",0))
        listTime.add(TimerModel("1 phút",1*60))
        listTime.add(TimerModel("15 phút",15*60))
        listTime.add(TimerModel("30 phút",30*60))
        listTime.add(TimerModel("60 phút",60*60))
        listTime.add(TimerModel("90 phút",90*60))
    }

    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view)
    {
        fun bind(time:TimerModel){
             itemView.status_timer.visibility = View.GONE
            if(time.time===timer) itemView.status_timer.visibility = View.VISIBLE
            itemView.tv_timer.text = time.title
            itemView.timer_select.setOnClickListener {
                accountFragment.setTime(time)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimerAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.timer_item,parent,false)
        val viewHolder = ViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: TimerAdapter.ViewHolder, position: Int) {
        holder.bind(listTime[position])
    }

    override fun getItemCount(): Int {
       return listTime.size
    }

    fun setTime(item:TimerModel){
        timer = 0;
        notifyDataSetChanged()
        timer = item.time
        notifyDataSetChanged()
    }
}
