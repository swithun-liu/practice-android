package com.example.expandcustomview

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.example.expandcustomview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding? = null
    private var isOpen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.openCloseBtn.setOnClickListener {
            when (binding.panel.state) {
                ExpandPanel.ExpandPanelState.CLOSING, ExpandPanel.ExpandPanelState.CLOSED -> binding.panel.expand()
                ExpandPanel.ExpandPanelState.OPENING, ExpandPanel.ExpandPanelState.OPENED -> binding.panel.close()
            }
        }
    }
}