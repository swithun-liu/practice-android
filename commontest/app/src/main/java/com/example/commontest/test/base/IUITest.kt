package com.example.commontest.test.base

import android.content.Context
import android.view.View
import android.view.ViewGroup

interface IUITest {
    fun getName(): String
    fun getViewHolderAndView(context: Context, parent: ViewGroup): Pair<Any, View>
    fun getCaseList(): Map<String, Any>
    fun bindState(vh: Any, data: Any)
}