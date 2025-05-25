package com.example.allviewdemo.lock

import java.util.concurrent.locks.ReentrantLock

class SQueue<T> constructor(private val size: Int) {
    private val lock = ReentrantLock()
    private val notFull = lock.newCondition()
    private val notEmpty = lock.newCondition()
    private val items = mutableListOf<T>()

    fun put(x: T, who: String = "") {
        lock.lock()
        try {
            while (items.size == size) {
                com.example.allviewdemo.lock.taketoomuch.sLog(
                    who,
                    TAG,
                    "[$items] is Full, stop!!!!!!!!!!!!!!!!!!!! put"
                )
                notFull.await()
            }
            com.example.allviewdemo.lock.taketoomuch.sLog(who, TAG, "[$items] is not Full, do put")
            items.add(x)
            notEmpty.signal()
        } finally {
            lock.unlock()
        }
    }

    fun take(who: String = ""): T {
        lock.lock()
        try {
            com.example.allviewdemo.lock.taketoomuch.sLog(who, TAG, "[$items] take")
            while (items.size == 0) {
                com.example.allviewdemo.lock.taketoomuch.sLog(
                    who,
                    TAG,
                    "[$items] is Empty, stop!!!!!!!!!!!!!!!!!!!! take"
                )
                notEmpty.await()
            }
            com.example.allviewdemo.lock.taketoomuch.sLog(who, TAG, "[$items] is not Empty, do take")
            val x = items[0]
            items.removeAt(0)
            notFull.signal()
            return x
        } finally {
            lock.unlock()
        }
    }

    fun putNotLock(x: T) {
        items.add(x)
    }

    fun takeNotLock(): T? {
        return try {
            items.removeAt(0)
        } catch (e: Exception) {
            null
        }
    }

    companion object {
        private const val TAG = "SQueue                                                                         "
    }
}

fun sLog(TAG: String, message: String) {
//    Log.d(TAG, "is not Full, do put")
    println("$TAG:     $message")
}