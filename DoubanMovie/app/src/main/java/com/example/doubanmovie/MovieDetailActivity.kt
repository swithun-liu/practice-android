package com.example.doubanmovie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MovieDetailActivity : AppCompatActivity() {

    private val TAG = "MovieDetailActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        val url = intent.getStringExtra("url").toString()
        Log.d(TAG, url)
    }
}