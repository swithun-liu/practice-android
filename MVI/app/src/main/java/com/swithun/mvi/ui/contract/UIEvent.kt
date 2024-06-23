package com.swithun.mvi.ui.contract

sealed interface UIEvent {
    data class ToastEvent(val msg: String) : UIEvent
    data object ClosePageEvent : UIEvent
}

fun UIEvent.toLogStr(): String {
    return when (this) {
        is UIEvent.ToastEvent -> "ToastEvent($msg)"
        UIEvent.ClosePageEvent -> "ClosePageEvent"
    }
}