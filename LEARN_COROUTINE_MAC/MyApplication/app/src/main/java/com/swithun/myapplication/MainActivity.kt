package com.swithun.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.swithun.myapplication.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val myCoroutineExceptionHandler =
        CoroutineExceptionHandler { coroutineContext, throwable ->
            swithunLog("error")
        }

    private val mainScope = CoroutineScope(Dispatchers.IO + SupervisorJob() + myCoroutineExceptionHandler)

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityMainBinding.inflate(layoutInflater)

        binding.btTest.setOnClickListener {
            mainScope.launch {
                test()
            }
        }

        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    private fun test() {
        swithunLog("current thread is : ${Thread.currentThread().name}")
    }
}