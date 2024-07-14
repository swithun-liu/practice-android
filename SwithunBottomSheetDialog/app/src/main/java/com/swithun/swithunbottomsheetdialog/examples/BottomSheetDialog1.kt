package com.swithun.swithunbottomsheetdialog.examples

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
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