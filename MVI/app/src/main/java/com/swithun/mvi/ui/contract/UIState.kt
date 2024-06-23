package com.swithun.mvi.ui.contract

sealed interface UIState {

    data object Loading : UIState

    data class Normal(val data1: String, val data2: Int?) : UIState

}

fun UIState.toLogStr(): String {
    return when (this) {
        UIState.Loading -> "Loading"
        is UIState.Normal -> "Normal[data1(${this.data1}), data2($data2)]"
    }
}

fun UIState.asNormal(block: (UIState.Normal) -> Unit) {
    when (this) {
        UIState.Loading -> {}
        is UIState.Normal -> block(this)
    }
}