package com.example.radiofm.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.radiofm.MainActivity
import com.example.radiofm.R



class PostCastMusicFragment(private var activity: MainActivity) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_post_cast_music, container, false)
    }

}
