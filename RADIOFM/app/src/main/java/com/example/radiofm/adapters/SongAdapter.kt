package com.example.radiofm.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.radiofm.AppActivity
import com.example.radiofm.R
import com.example.radiofm.fragments.PodCastFragment
import com.example.radiofm.fragments.PostCastMusicFragment
import com.example.radiofm.models.Song
import kotlinx.android.synthetic.main.song_item.view.*

class SongAdapter(private val podCastFragment: PodCastFragment):RecyclerView.Adapter<SongAdapter.ViewHolder>() {

    private var songList:MutableList<Song> = arrayListOf();

    private var urlIsPlay:String = ""

    fun setData(songs:MutableList<Song>){
        songList = songs
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view)
    {
        fun bind(data:Song){
            Glide.with(itemView).load(data.url_image).into(itemView.image_song)
            itemView.tv_name_song.text = data.name
            if(data.link != urlIsPlay) itemView.song_is_play.visibility = View.GONE
            if(data.link === urlIsPlay) itemView.song_is_play.visibility = View.VISIBLE
            itemView.song_item.setOnClickListener {
                podCastFragment.playSong(data);
            }
        }
    }

    fun setDataIsPlay(url:String){
        urlIsPlay = ""
        notifyDataSetChanged()
        urlIsPlay = url
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.song_item,parent,false)
        val viewHolder = ViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: SongAdapter.ViewHolder, position: Int) {
        holder.bind(songList[position])
    }

    override fun getItemCount(): Int {
        return songList.size;
    }
}
