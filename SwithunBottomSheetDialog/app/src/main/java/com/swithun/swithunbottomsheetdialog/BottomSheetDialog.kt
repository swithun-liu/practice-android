package com.swithun.swithunbottomsheetdialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment

class BottomSheetDialog: DialogFragment() {

    private var contentView: View? = null

    override fun getTheme(): Int {
        return R.style.com_SwithunBottomSheetDialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        contentView = inflater.inflate(R.layout.bottom_sheet_dialog, container, false)
        return contentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (contentView as? ViewGroup)?.findViewById<TextView>(R.id.testClick)?.setOnClickListener {
            Toast.makeText(contentView!!.context, "hahah", Toast.LENGTH_SHORT).show()
        }
        (contentView as? ViewGroup)?.findViewById<TextView>(R.id.clickButton)?.setOnClickListener {
            Toast.makeText(contentView!!.context, "hahah", Toast.LENGTH_SHORT).show()
        }
    }

}