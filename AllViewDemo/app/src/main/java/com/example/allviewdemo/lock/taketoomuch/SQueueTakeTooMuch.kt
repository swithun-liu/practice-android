package com.example.allviewdemo.lock.taketoomuch

import java.util.concurrent.locks.ReentrantLock

class SQueueTakeTooMuch<T> constructor(private val size: Int) {
    private val lock = ReentrantLock()
    private val notFull = lock.newCondition()
    private val notEmpty = lock.newCondition()
    private val items = mutableListOf<T>()

    fun put(x: T, who: String = "") {
        lock.lock()
        try {
            while (items.size == size) {
                sLog(who, TAG, "[$items] is Full, stop!!!!!!!!!!!!!!!!!!!! put")
                notFull.await()
            }
            sLog(who, TAG, "[$items] is not Full, do put")
            items.add(x)
            notEmpty.signalAll()
        } finally {
            lock.unlock()
        }
    }

    fun take(who: String): T {
        lock.lock()
        try {
            sLog(who, TAG, "[$items] take")
            if (items.size == 0) {
                sLog(who, TAG, "[$items] is Empty, stop!!!!!!!!!!!!!!!!!!!! take")
                notEmpty.await()
            }
            sLog(who, TAG, "[$items] is not Empty, do take")
            val x = items[0]
            items.removeAt(0)
            notFull.signal()
            return x
        } finally {
            lock.unlock()
        }
    }

    companion object {
        private const val TAG = "SQueue                                                                         "
    }
}

fun sLog(who: String , TAG: String, message: String) {
    println("$TAG:         ${Thread.currentThread()}       who: $who      |     $message")
}