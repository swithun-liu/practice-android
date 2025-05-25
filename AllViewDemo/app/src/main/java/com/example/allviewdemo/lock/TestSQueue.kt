package com.example.allviewdemo.lock

import kotlin.random.Random

class PutThread(private val name: String, private val queue: SQueue<String>) : Thread() {
    override fun run() {
        for (i in 1..8) {
            sleep(Random.nextLong(4, 5))
            queue.put("[$name: $i]")
        }
    }
}

class TakeThread(private val name: String, private val queue: SQueue<String>): Thread() {
    override fun run() {
        for (i in 1..17) {
            sleep(Random.nextLong(400, 600))
            com.example.allviewdemo.lock.taketoomuch.sLog(
                name,
                "MAIN",
                "TakeThread [$name] get value: ${queue.take()}"
            )
        }
    }
}

class PutThreadNotLock(private val queue: SQueue<Int>) : Thread() {
    override fun run() {
        for (i in 1..8) {
            sleep(Random.nextLong(1, 50))
            queue.putNotLock(i)
        }
    }
}


class TakeThreadNotLock(private val queue: SQueue<Int>): Thread() {
    override fun run() {
        for (i in 1..9) {
            sleep(Random.nextLong(1, 40))
            com.example.allviewdemo.lock.taketoomuch.sLog(
                name,
                "MAIN",
                "TakeThread get value: ${queue.takeNotLock()}"
            )
        }
    }
}


fun main() {
    val queue = SQueue<String>(4)
    val putThread1 = PutThread("队1", queue)
    val putThread2 = PutThread("队2", queue)
    val takeThread = TakeThread("取1", queue)

    putThread1.start()
    putThread2.start()
    takeThread.start()
}

// 2 consumer 1 producer
//fun main() {
//    val queue = SQueue<String>(4)
//    val putThread = PutThread("队1", queue)
//    val takeThread1 = TakeThread("取1", queue)
//    val takeThread2 = TakeThread("取2", queue)
//
////    val putThread = PutThreadNotLock(queue)
////    val takeThread = TakeThreadNotLock(queue)
//    putThread.start()
//    takeThread1.start()
//    takeThread2.start()
//}
