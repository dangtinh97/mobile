package com.example.reviewphim.models

data class MovieListModel (
    val id:Int,
    val title:String,
    val created:String,
    val picture_url:String,
    val video_url:String,
    val name_channel:String,
    var count_viewer:Int,
    var count_reaction:Int,
    var isLike:Boolean = false
    )
