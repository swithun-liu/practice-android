package com.example.commontest

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.example.commontest.databinding.ActivityMainBinding
import com.example.commontest.test.AlphaHeightLayoutUITest

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initUITests(binding)
    }

    private fun initUITests(binding: ActivityMainBinding) {
        listOf(
            AlphaHeightLayoutUITest()
        ).forEach { uiTest ->
            val name = uiTest.getName()
            val testButton = Button(binding.root.context).also {
                it.text = name
                it.setOnClickListener {
                    binding.contentContainer.isVisible = true

                    val (vh, v) = uiTest.getViewHolderAndView(
                        binding.root.context,
                        binding.contentArea
                    )
                    binding.contentArea.removeAllViews()
                    binding.contentArea.addView(v)

                    val caseList = uiTest.getCaseList()

                    binding.caseList.removeAllViews()
                    caseList.forEach { (name, data) ->
                        binding.caseList.addView(Button(binding.root.context).also {
                            it.text = name
                            uiTest.bindState(vh, data)
                        })
                    }
                }
            }
            binding.testList.addView(testButton)
        }

        binding.back.setOnClickListener {
            binding.contentContainer.isVisible = false
        }
    }
}