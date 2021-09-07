package com.example.radiofm.fragments

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.radiofm.AppActivity
import com.example.radiofm.R
import com.example.radiofm.adapters.TimerAdapter
import com.example.radiofm.models.TimerModel
import kotlin.system.exitProcess


class AccountFragment(private val appActivity: AppActivity) : Fragment() {

    private var timerSet= TimerModel("Không hẹn giờ",0);

    private var timerAdapter= TimerAdapter(this@AccountFragment)

    private var setTimed = false

    private lateinit var countDownTimer:CountDownTimer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_account, container, false)

        view.findViewById<RecyclerView>(R.id.timer_recycler_view).apply {
            val linearLayoutManager = LinearLayoutManager(context)
            layoutManager = linearLayoutManager
            adapter = timerAdapter
        }
        view.findViewById<ImageView>(R.id.open_timer).setOnClickListener {
            view.findViewById<RelativeLayout>(R.id.modal_timer).visibility = View.VISIBLE
        }

        view.findViewById<Button>(R.id.close_timer).setOnClickListener {
            view.findViewById<RelativeLayout>(R.id.modal_timer).visibility = View.GONE
        }

        view.findViewById<Button>(R.id.save_timer).setOnClickListener {

            timerAdapter.setTime(timerSet)
            if(timerSet.time>0){
                countDownTimer = object : CountDownTimer((timerSet.time*1000).toLong(),1000){
                    override fun onTick(millisUntilFinished: Long) {
                        view.findViewById<TextView>(R.id.second_finished).text = (millisUntilFinished/1000).toString()+ " (s)"
                    }

                    override fun onFinish() {
                        exitProcess(-1)
                    }
                }.start()
                view.findViewById<TextView>(R.id.second_finished).visibility = View.VISIBLE
                setTimed = true
            }else{
                view.findViewById<TextView>(R.id.second_finished).visibility = View.GONE
                if(setTimed) countDownTimer.cancel()
            }

            view.findViewById<RelativeLayout>(R.id.modal_timer).visibility = View.GONE

        }


        return view;
    }

    fun setTime(item:TimerModel){
        timerSet = item
        timerAdapter.setTime(timerSet)
    }

}
