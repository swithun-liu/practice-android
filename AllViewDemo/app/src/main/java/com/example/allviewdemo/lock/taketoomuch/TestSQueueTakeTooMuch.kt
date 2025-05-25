package com.example.allviewdemo.lock.taketoomuch

import kotlin.random.Random


class PutThread(private val name: String, private val queue: SQueueTakeTooMuch<String>) : Thread() {
    override fun run() {
        for (i in 1..8) {
            sleep(Random.nextLong(400, 500))
            queue.put("[$name: $i]", name)
        }
    }
}

class TakeThread(private val name: String, private val queue: SQueueTakeTooMuch<String>): Thread() {
    override fun run() {
        for (i in 1..17) {
            sleep(Random.nextLong(4, 6))
            sLog(
                name,
                "MAIN",
                "TakeThread [$name] get value: ${queue.take(name)}"
            )
        }
    }
}

fun main() {
    val queue = SQueueTakeTooMuch<String>(4)
    val putThread = PutThread("队1", queue)
    val takeThread1 = TakeThread("取1", queue)
    val takeThread2 = TakeThread("取2", queue)

    putThread.start()
    takeThread1.start()
    takeThread2.start()
}
