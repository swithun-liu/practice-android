package com.swithun.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.swithun.myapplication.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val mainScope = CoroutineScope(SupervisorJob() + Dispatchers.Main + Dispatchers.IO)

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