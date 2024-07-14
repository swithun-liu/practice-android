package com.swithun.swithunbottomsheetdialog.examples

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.swithun.swithunbottomsheetdialog.BottomSheetDialog
import com.swithun.swithunbottomsheetdialog.BottomSheetDialogLayout
import com.swithun.swithunbottomsheetdialog.R

class BottomSheetDialog2: BottomSheetDialog() {
    override fun onCreateContentView(
        inflater: LayoutInflater,
        contentViewWrapper: BottomSheetDialogLayout,
        savedInstanceState: Bundle?
    ) {
        inflater.inflate(R.layout.bottom_sheet_dialog_3, contentViewWrapper, true)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.testClick)?.setOnClickListener {
            Toast.makeText(view.context, "hahah", Toast.LENGTH_SHORT).show()
        }
        view.findViewById<TextView>(R.id.clickButton)?.setOnClickListener {
            Toast.makeText(view.context, "hahah", Toast.LENGTH_SHORT).show()
        }
    }
}