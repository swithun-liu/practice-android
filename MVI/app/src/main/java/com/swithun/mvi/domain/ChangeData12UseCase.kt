package com.swithun.mvi.domain

import com.swithun.mvi.data.Data1Repository
import com.swithun.mvi.data.Data2Repository
import kotlinx.coroutines.flow.flow

class ChangeData12UseCase(private val data1Repository: Data1Repository, private val data2Repository: Data2Repository) {
    fun execute() = flow<Pair<String, Int?>> {
        ChangeData1UseCase(data1Repository).execute().collect {
            emit("Data1" to it)
        }
        emit("Data2" to ChangeData2UseCase(data2Repository).execute())
    }
}