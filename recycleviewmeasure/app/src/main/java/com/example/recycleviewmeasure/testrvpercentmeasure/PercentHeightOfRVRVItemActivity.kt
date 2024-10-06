package com.example.recycleviewmeasure.testrvpercentmeasure

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recycleviewmeasure.R
import com.example.recycleviewmeasure.databinding.VBinding

class PercentHeightOfRVRVItemActivity : AppCompatActivity() {
    private val adapter = PercentHeightOfRVRVItemRVAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = VBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.testRv.layoutManager = LinearLayoutManager(this)
        binding.testRv.adapter = adapter
        adapter.submitList(listOf(
            RVItemData(10),
            RVItemData(20),
            RVItemData(30),
            RVItemData(40),
            RVItemData(50),
            RVItemData(60),
            RVItemData(70),
            RVItemData(80),
            RVItemData(90),
            RVItemData(100),
            RVItemData(120),
            RVItemData(130),
        ))
        binding.refresh.setOnClickListener {
            adapter.submitList(listOf(
                RVItemData(10),
                RVItemData(20),
                RVItemData(30),
                RVItemData(40),
                RVItemData(50),
                RVItemData(60),
                RVItemData(70),
                RVItemData(80),
                RVItemData(90),
                RVItemData(100),
                RVItemData(120),
                RVItemData(130),
            ))
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}