package com.example.learn_coroutine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.learn_coroutine.databinding.ActivityMainBinding
import kotlin.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        val continuation = suspend {
            println("In Coroutine.")
            5
        }.startCoroutine(object : Continuation<Int> {
            override fun resumeWith(result: Result<Int>) {
                println("Coroutine End: $result")
            }

            override val context = EmptyCoroutineContext
        })
    }
}
