package com.swithun.swithunbottomsheetdialog.examples

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.swithun.swithunbottomsheetdialog.BottomSheetDialog
import com.swithun.swithunbottomsheetdialog.R

class BottomSheetDialog1: BottomSheetDialog() {
    override fun onCreateContentView(
        inflater: LayoutInflater,
        contentViewWrapper: ViewGroup,
        savedInstanceState: Bundle?
    ) {
        inflater.inflate(R.layout.bottom_sheet_dialog_1, contentViewWrapper, true)
    }
}