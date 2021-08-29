package com.example.myapplication.ktfile

import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.createCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.startCoroutine

fun main() {
    fun1()
    fun2()
}

/**
 * 协程的创建+协程的运行
 */
fun fun1() {
    //k 1.1 协程的创建
    val continuation = suspend {
        println("In coroutine")
        1
    }.createCoroutine(object : Continuation<Int> {
        override fun resumeWith(result: Result<Int>) {
            println("Coroutine End: $result")
        }

        override val context: CoroutineContext
            get() = EmptyCoroutineContext
    })
    /*
    createCoroutine()
        Receiver: 协程体
        completion: 完成回调
        Continuation: 返回值--通过它触发协程的启动
     */
    // 1.2 协程的运行
    continuation.resume(Unit)

    /*
    In coroutine
    Coroutine End: Success(1)
     */
}

/**
 * 协程的创建和运行
 */
fun fun2() {
    val continuation = suspend {
        println("In coroutine")
        1
    }.startCoroutine(object : Continuation<Int> {
        override fun resumeWith(result: Result<Int>) {
            println("Coroutine End: $result")
        }

        override val context: CoroutineContext
            get() = EmptyCoroutineContext
    })

    /*
    In coroutine
    Coroutine End: Success(1)
     */
}
