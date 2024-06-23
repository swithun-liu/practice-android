package com.swithun.mvi.data

import kotlinx.coroutines.delay

class Data1Repository {

    private var data1: Int? = null

    fun getData1(): Int? {
        return data1
    }

    suspend fun changeData1(): Boolean {
        if (data1 == null) {
            data1 = 1
        }
        delay(1000)
        return data1?.let {
            data1 = it + 1
            true
        } ?: false
    }

    fun clearCache(): Boolean {
        data1 = null
        return true
    }

}