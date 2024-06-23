package com.swithun.mvi.domain

import com.swithun.mvi.data.Data1Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class ChangeData1UseCase(private val repository: Data1Repository) {

    fun execute(): Flow<Int?> = flow {
        // change1
        repository.changeData1()
        emit(repository.getData1())

        // change1 again
        repository.changeData1()
        emit(repository.getData1())
    }

}