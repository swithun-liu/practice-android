package com.example.threadpool

import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.Volatile


class SimpleThreadPool2 {

    @Volatile
    private var terminated = false

    private val threadFactory = object : ThreadFactory {
        private val counter = AtomicInteger()

        override fun newThread(r: Runnable?): Thread {
            return Thread(r, "default-pool-${counter.getAndIncrement()}")
        }

    }

    private val tasks = LinkedBlockingQueue<Runnable>()
    private val workers = mutableListOf<Worker>().also {
        for (i in 0..Runtime.getRuntime().availableProcessors()) {
            it.add(Worker())
        }
    }

    fun execute(command: Runnable) {
        try {
            tasks.put(command)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    fun stop() {
        terminated = true
    }

    fun invokerAllThreadInterrupt() {
        workers.forEach { worker ->
            worker.thread.interrupt()
        }
    }

    inner class Worker : Runnable {
        val thread = threadFactory.newThread(this)

        init {
            thread.start()
        }

        override fun run() {
            val thread = Thread.currentThread()
            while (!terminated && !thread.isInterrupted) {
                try {
                    val t = tasks.take()
                    t.run()
                } catch (e: InterruptedException) {
                    thread.interrupt()
                    break
                }
            }
        }

    }
}


fun main() {
    val pool = SimpleThreadPool2()

    // 6 建立多个任务丢进线程池运行
    (1..50).forEach { i: Int ->
        try {
            pool.execute {
                println("swithun-xxxx pool2.execute: [task: $i] in ${Thread.currentThread().name}")
            }
        } catch (e: Exception) {
            println("swithun-xxxx pool2.execute err : ${e.printStackTrace()}")
        }
    }
}