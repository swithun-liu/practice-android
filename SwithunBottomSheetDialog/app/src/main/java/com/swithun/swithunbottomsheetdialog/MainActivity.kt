package com.swithun.swithunbottomsheetdialog

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.swithun.swithunbottomsheetdialog.examples.BottomSheetDialog0
import com.swithun.swithunbottomsheetdialog.examples.BottomSheetDialog1
import com.swithun.swithunbottomsheetdialog.examples.BottomSheetDialog2
import com.swithun.swithunbottomsheetdialog.examples.BottomSheetDialog3
import com.swithun.swithunbottomsheetdialog.examples.BottomSheetDialog4
import com.swithun.swithunbottomsheetdialog.examples.BottomSheetDialog5
import com.swithun.swithunbottomsheetdialog.examples.BottomSheetDialog6

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
        listOf(
            generateButton(this, "0") { BottomSheetDialog0() },
            generateButton(this, "1") { BottomSheetDialog1() },
            generateButton(this, "2") { BottomSheetDialog2() },
            generateButton(this, "3") { BottomSheetDialog3() },
            generateButton(this, "4") { BottomSheetDialog4() },
            generateButton(this, "5") { BottomSheetDialog5() },
            generateButton(this, "6") { BottomSheetDialog6() },
        ).forEach { bt ->
            this.findViewById<LinearLayout>(R.id.main).also { main ->
                main.addView(bt)
            }
        }
    }

    private fun generateButton(
        context: Context,
        name: String,
        dialogBuilder: () -> BottomSheetDialog
    ): View {
        return Button(context).also {
            it.text = name
            it.setOnClickListener {
                dialogBuilder().show(supportFragmentManager, name)
            }
        }
    }
}