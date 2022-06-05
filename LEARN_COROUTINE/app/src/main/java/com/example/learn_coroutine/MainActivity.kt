package com.example.learn_coroutine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.learn_coroutine.databinding.ActivityMainBinding
import kotlinx.coroutines.runBlocking
import swithunPrint
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val executor = Executors.newScheduledThreadPool(1) { runnable ->
        Thread(runnable, "Scheduler").apply { isDaemon = true }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        runBlocking {
            swithunPrint("[THREAD-${Thread.currentThread().name}] before swithunDelay")
            swithunDelay(2000)
            swithunPrint("[THREAD-${Thread.currentThread().name}] after swithunDelay")
        }

    }

    suspend fun swithunDelay(time: Long, unit: TimeUnit = TimeUnit.MILLISECONDS) {
        if (time <= 0) {
            return
        }
        suspendCoroutine<Unit> { continuation ->
            executor.schedule({
                swithunPrint("[THREAD-${Thread.currentThread()}] then continuation resume")
                swithunPrint("[THREAD-${Thread.currentThread().name}] then continuation resume")
                continuation.resume(Unit)
            },
            time, unit)
        }
    }
}
