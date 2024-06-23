package com.swithun.mvi.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class Data2Repository {

    private var innerData2: MutableStateFlow<Int?> = MutableStateFlow(null)
    val data2: StateFlow<Int?> = innerData2.asStateFlow()

    suspend fun changeData(): Boolean {
        if (data2.value == null) {
            innerData2.value = 1
            return true
        }
        delay(1000)
        return innerData2.value?.let {
            innerData2.value = it + 1
            true
        } ?: false
    }

    fun clearCache(): Boolean {
        innerData2.value = null
        return true
    }

}