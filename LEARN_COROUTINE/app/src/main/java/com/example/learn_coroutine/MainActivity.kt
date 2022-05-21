package com.example.learn_coroutine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.learn_coroutine.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import swithunLog
import swithunPrint
import java.lang.Thread.sleep
import kotlin.concurrent.thread
import kotlin.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val ref = ::notSuspend
        ref.call(object : Continuation<Int> {
            override val context = EmptyCoroutineContext

            override fun resumeWith(result: Result<Int>) {
                swithunPrint("resumeWith: ${result.getOrNull()}")
            }
        })
    }

    suspend fun notSuspend() = suspendCoroutine<Int> { continuation ->
        thread {
            swithunPrint("thread in ${Thread.currentThread().name} - 1")
            sleep(1000)
            swithunPrint("thread in ${Thread.currentThread().name} - 2")
            continuation.resume(100)
        }
    }
}