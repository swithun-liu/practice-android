package com.swithun.coroutine

import kotlinx.atomicfu.*

private val _state = atomic(1)

fun main() {
    _state.loop {

    }
}
