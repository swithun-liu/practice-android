package com.swithun.learndialog.mydialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.annotation.StyleRes
import androidx.annotation.UiContext
import com.swithun.learndialog.R

class MyDialog @JvmOverloads constructor(
    @UiContext context: Context,
    @StyleRes themeResId: Int = 0,
) : Dialog(context, themeResId) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_layout)
    }

}