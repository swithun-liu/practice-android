package com.swithun.mvi.domain

import com.swithun.mvi.data.Data2Repository

class ChangeData2UseCase(private val data2Repository: Data2Repository) {

    suspend fun execute(): Int? {
        data2Repository.changeData()
        return data2Repository.data2.value
    }

}