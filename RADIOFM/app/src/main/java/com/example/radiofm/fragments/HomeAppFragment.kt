package com.example.radiofm.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.example.radiofm.AppActivity
import com.example.radiofm.R
import com.example.radiofm.adapters.HomeViewPagerAdapter
import com.google.android.material.tabs.TabLayout


class HomeAppFragment(private val activity: AppActivity) : Fragment() {

     var podCastFragment = PodCastFragment(activity)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val adapter = HomeViewPagerAdapter(childFragmentManager)
        adapter.addFragment(RadioFragment(activity),"Radio")
        adapter.addFragment(podCastFragment,"Podcast")

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home_app, container, false)
        val viewPager = view.findViewById<ViewPager>(R.id.home_viewpager)
            viewPager.setAdapter(adapter)
        view.findViewById<TabLayout>(R.id.home_tab_layout).setTabsFromPagerAdapter(adapter)
        viewPager.setCurrentItem(0);
        view.findViewById<TabLayout>(R.id.home_tab_layout).setupWithViewPager(viewPager)
        return view;
    }


    fun setUrlIsPlay(url:String){
        podCastFragment.setUrlIsPlay(url)
    }

}
