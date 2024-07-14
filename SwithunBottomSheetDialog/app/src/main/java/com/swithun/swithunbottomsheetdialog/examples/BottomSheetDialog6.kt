package com.swithun.swithunbottomsheetdialog.examples

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.toDrawable
import com.swithun.swithunbottomsheetdialog.BottomSheetDialog
import com.swithun.swithunbottomsheetdialog.BottomSheetDialogLayout
import com.swithun.swithunbottomsheetdialog.R

class BottomSheetDialog6: BottomSheetDialog() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateContentView(
        inflater: LayoutInflater,
        contentViewWrapper: BottomSheetDialogLayout,
        savedInstanceState: Bundle?
    ) {
        inflater.inflate(R.layout.bottom_sheet_dialog_1, contentViewWrapper, true)
        contentViewWrapper.asIBottomSheetDialogLayoutSetting().customStateList = listOf(
            BottomSheetDialogLayout.CustomHeight.Bottom(200),
            BottomSheetDialogLayout.CustomHeight.Percent(0.4f),
            BottomSheetDialogLayout.CustomHeight.Highest(200)
        )
        contentViewWrapper.asIBottomSheetDialogLayoutSetting().bgMask = Color.valueOf(255f, 0f, 255f, 0.8f).toDrawable()
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