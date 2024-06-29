package com.swithun.learndialog

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.swithun.learndialog.mydialog.MyDialog

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        findViewById<Button>(R.id.openBtn).setOnClickListener {
            val dialog = MyDialog(this)
            dialog.show()
            Log.d("swithun-xxxx", "ownerActivity ${dialog.ownerActivity}")
        }

        findViewById<Button>(R.id.openPopupWindowBtn).setOnClickListener {
            val popupWindow = PopupWindow(
                LayoutInflater.from(this).inflate(
                    R.layout.popup_window_layout,
                    null,
                    false
                ),
                2000,
                2000
            )
            popupWindow.setBackgroundDrawable(ColorDrawable(0x000000FF));
            popupWindow.showAsDropDown(findViewById<Button>(R.id.openPopupWindowBtn))
        }
    }
}