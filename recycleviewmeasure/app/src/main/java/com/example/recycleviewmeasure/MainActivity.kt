package com.example.recycleviewmeasure

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.recycleviewmeasure.databinding.ActivityMainBinding
import com.example.recycleviewmeasure.testrvpercentmeasure.PercentHeightOfRVRVItemRVAdapter
import com.example.recycleviewmeasure.testrvpercentmeasure.PercentHeightOfRVRVItemActivity

class MainActivity : AppCompatActivity() {
    private val adapter = PercentHeightOfRVRVItemRVAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.percentHeightOfRVRVItem.setOnClickListener {
            this.startActivity(Intent(this, PercentHeightOfRVRVItemActivity::class.java))
        }
        binding.hv.setOnClickListener {
            this.startActivity(Intent(this, HVActivity::class.java))
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}