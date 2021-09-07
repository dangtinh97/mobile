package com.example.reviewphim.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class AppViewPagerAdapter(fm:FragmentManager): FragmentPagerAdapter(fm) {


    private val fragmentList : MutableList<Fragment> = arrayListOf()
    private val titleList:MutableList<String> = arrayListOf()



    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    fun addFragment(fragment: Fragment,title:String){
        fragmentList.add(fragment)
        titleList.add(title)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titleList[position]
    }


}
