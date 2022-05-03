package com.example.learn_coroutine

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface MyAPI {

    @GET("/comments")
    fun getComments(): Call<List<Comment>>

    @GET("/comments")
    suspend fun betterGetComments(): Response<List<Comment>>
}