package com.swithun.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.swithun.myapplication.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_second)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val extraData = intent.getStringExtra("extra_data")
        Toast.makeText(this, "extra data is $extraData", Toast.LENGTH_SHORT).show()
        Log.d("SecondActivity", "extra data is $extraData")

        binding.button2.setOnClickListener {
            testIntentReturnData()
        }
    }

    override fun onBackPressed() {
        testIntentReturnData()
    }

    fun testIntentReturnData() {
        val intent = Intent()
        intent.putExtra("data_return", "Hello FirstActivity")
        setResult(RESULT_OK, intent)
        finish()
    }
}