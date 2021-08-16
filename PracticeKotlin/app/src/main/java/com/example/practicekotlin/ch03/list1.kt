package com.example.practicekotlin.ch03

import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.createCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.startCoroutine

fun main() {
    val continuation1 = suspend {
        println("In Coroutine1.")
        1
    }.createCoroutine(object : Continuation<Int> {
        override fun resumeWith(result: Result<Int>) {
            println("Coroutine1 End: $result")
        }

        override val context = EmptyCoroutineContext
    })

    continuation1.resume(Unit)

    val continuation2 = suspend {
        println("In Coroutine2")
        2
    }.startCoroutine(object : Continuation<Int> {
        override val context: CoroutineContext
            get() = EmptyCoroutineContext

        override fun resumeWith(result: Result<Int>) {
            println("Coroutine2 End: $result")
        }

    })
}