package com.swithun.swithunbottomsheetdialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment

abstract class BottomSheetDialog: DialogFragment() {

    private var contentView: View? = null

    override fun getTheme(): Int {
        return R.style.com_SwithunBottomSheetDialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val contentViewWrapper = inflater.inflate(R.layout.bottom_sheet_dialog_layout, container, false)
        contentView = contentViewWrapper
        onCreateContentView(inflater, contentViewWrapper as BottomSheetDialogLayout, savedInstanceState)
        contentViewWrapper.asIBottomSheetDialogLayoutSetting().init()
        return contentView
    }

    abstract fun onCreateContentView(
        inflater: LayoutInflater,
        contentViewWrapper: BottomSheetDialogLayout,
        savedInstanceState: Bundle?
    )

}