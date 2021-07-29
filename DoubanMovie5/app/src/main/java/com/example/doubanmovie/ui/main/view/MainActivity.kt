package com.example.doubanmovie.ui.main.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.doubanmovie.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var movieView: MovieView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initMVVM()
    }

    private fun initMVVM() {
        movieView = MovieView(this, binding)
    }

    override fun onStop() {
        super.onStop()
        movieView.onStop()
    }
}