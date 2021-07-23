package com.example.doubanmovie.logic.model

import java.io.Serializable

data class Episode(
    val title: String, val cover: String, val rate: String, val url: String
) : Serializable {}