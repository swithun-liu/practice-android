package com.swithun.coroutine

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun testDispatcher() {
    withContext(Dispatchers.IO) {
        println("IO")
    }
    withContext(Dispatchers.Main) {
        println("Main")
    }
}