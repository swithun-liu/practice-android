package com.example.doubanmovie.entity

import java.io.Serializable

data class MovieDetail(
    val actor: List<Actor>,
    val aggregateRating: AggregateRating,
    val author: List<Author>,
    val datePublished: String,
    val description: String,
    val director: List<Director>,
    val duration: String,
    val genre: List<String>,
    val image: String,
    val name: String,
    val url: String
): Serializable {
}