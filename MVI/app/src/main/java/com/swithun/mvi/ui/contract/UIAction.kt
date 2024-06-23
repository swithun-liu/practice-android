package com.swithun.mvi.ui.contract

sealed interface UIAction {

    class ClickAAAction(val someInfo: String): UIAction
    class ClickABAction(val someInfo: String): UIAction
    class ClickBAction(val someInfo: String): UIAction
    data object ClosePageAction: UIAction
}

fun UIAction.toLogStr(): String {
    return when(this) {
        is UIAction.ClickAAAction -> "ClickAAAction[${this.someInfo}]"
        is UIAction.ClickABAction -> "ClickABAction[${this.someInfo}]"
        is UIAction.ClickBAction -> "ClickBAction[${this.someInfo}]"
        UIAction.ClosePageAction -> "ClosePageAction"
    }
}