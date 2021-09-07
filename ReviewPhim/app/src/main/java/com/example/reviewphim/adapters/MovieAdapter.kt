package com.example.reviewphim.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.reviewphim.R
import com.example.reviewphim.fragments.ListMovieFragment
import com.example.reviewphim.models.MovieListModel
import kotlinx.android.synthetic.main.movie_item.view.*

class MovieAdapter(private val listMovieFragment: ListMovieFragment):RecyclerView.Adapter<MovieAdapter.ViewHolder>() {


    private var movies:MutableList<MovieListModel> = arrayListOf()

    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        fun bind(data:MovieListModel){
            itemView.tv_title_movie_item.text = data.title;
            itemView.name_channel_movie_item.text = data.name_channel
            itemView.tv_viewer_time.text = (data.count_viewer.toString() + " lượt xem - "+data.created)
            if(data.picture_url!="") Glide.with(itemView).load(data.picture_url).into(itemView.thumbnail_movie)
            itemView.movie_item.setOnClickListener {
                listMovieFragment.onClickListenerAdapter(data)
            }
        }
    }

    fun setData(moviesData:MutableList<MovieListModel>){
        movies = moviesData;
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item,parent,false)
        val viewHolder = ViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount(): Int {
        return movies.size
    }

     fun setViewer(data:MovieListModel){
        notifyItemChanged(movies.indexOf(data))
    }
}
