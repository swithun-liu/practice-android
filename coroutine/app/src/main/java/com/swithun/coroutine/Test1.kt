package com.swithun.coroutine

import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

suspend fun testDelay(time: Long): Int {
    val a = testDelay1(time)
    val b = testDelay2(time)
    val c = testDelay3(time, "haha")
    return a + b + c
}
suspend fun testDelay1(time: Long): Int {
    delay(time)
    return 1
}

suspend fun testDelay2(time: Long): Int {
    delay(time)
    return 2
}
suspend fun testDelay3(time: Long, msg: String): Int {
    delay(time)
    println(msg)
    delay(time)
    return 3
}

suspend fun testDelay4() = suspendCancellableCoroutine<Unit> { con ->
    con.resume(Unit)
}