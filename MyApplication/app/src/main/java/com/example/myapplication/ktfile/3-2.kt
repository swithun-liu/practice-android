package com.example.myapplication.ktfile

import kotlin.concurrent.thread
import kotlin.coroutines.suspendCoroutine

fun main() {
}

/*
函数类型：suspend(Int) -> Unit
 */
suspend fun suspendFun01(a: Int) {
    return
}

/*
函数类型：suspend(String, String) -> Int
 */
suspend fun suspendFun02(a: String, b: String) = suspendCoroutine<Int> { continuation ->
    thread {
        continuation.resumeWith(Result.success(1))
    }
}