package com.example.threadpool

import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue

class SimpleThreadPool(threadSize: Int) {
    /** 1. 任务队列: 线程安全*/
    val taskQueue: BlockingQueue<Runnable> = LinkedBlockingQueue(10)

    /** 2. 所有线程: worker */
    val threads = ArrayList<SimpleThread>(threadSize)

    init {
        // 3 启动所有线程
        (1..threadSize).forEach { i: Int ->
            SimpleThread("swithun-task-thread-$i").also {
                it.start()
                threads.add(it)
            }
        }
    }

    fun execute(task: Runnable) {
        // 4 将任务放入“任务队列”
        taskQueue.put(task)
    }

    inner class SimpleThread(name: String) : Thread(name) {
        override fun run() {
            while (true) {
                // 5 每个线程都会不断的从“任务队列”中取任务 & 执行 & 哪个线程取到就在哪个线程执行
                val task: Runnable? = try {
                    taskQueue.take()
                } catch (e: Exception) {
                    println("swithun-xxxx taskQueue.take err : ${e.printStackTrace()}")
                    null
                }
                task?.run()
            }
        }
    }
}

fun main() {
    val pool = SimpleThreadPool(3)

    // 6 建立多个任务丢进线程池运行
    (1..5).forEach { i: Int ->
        try {
            pool.execute {
                println("swithun-xxxx pool.execute: [task: $i] in ${Thread.currentThread().name}")
            }
        } catch (e: Exception) {
            println("swithun-xxxx pool.execute err : ${e.printStackTrace()}")
        }
    }
}
