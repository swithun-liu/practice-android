package com.swithun.swithunbottomsheetdialog.examples

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.swithun.swithunbottomsheetdialog.R

class MaterialBottomSheetDialog: BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_dialog_0, container, false)
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