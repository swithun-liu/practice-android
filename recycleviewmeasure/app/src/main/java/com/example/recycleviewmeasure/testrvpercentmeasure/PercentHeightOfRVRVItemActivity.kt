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
            RVItemData.OtherItem("10".repeat(10)),
            RVItemData.PercentItem(10),
            RVItemData.OtherItem("10".repeat(20)),
            RVItemData.PercentItem(20),
            RVItemData.OtherItem("10".repeat(30)),
            RVItemData.PercentItem(30),
        ))
        binding.refresh.setOnClickListener {
            adapter.submitList(listOf(
                RVItemData.OtherItem("10".repeat(10)),
                RVItemData.PercentItem(10),
                RVItemData.OtherItem("10".repeat(200)),
                RVItemData.PercentItem(20),
                RVItemData.OtherItem("10".repeat(300)),
                RVItemData.PercentItem(30),
                RVItemData.OtherItem("10".repeat(10)),
                RVItemData.PercentItem(10),
                RVItemData.OtherItem("10".repeat(200)),
                RVItemData.PercentItem(20),
                RVItemData.OtherItem("10".repeat(300)),
                RVItemData.PercentItem(30),
            ))
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}