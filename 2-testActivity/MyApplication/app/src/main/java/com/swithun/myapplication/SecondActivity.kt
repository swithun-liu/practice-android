package com.swithun.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.swithun.myapplication.databinding.ActivitySecondBinding

class SecondActivity : BaseActivity() {

    private lateinit var binding: ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val data1 = intent.getStringExtra("param1")
        val data2 = intent.getStringExtra("param2")
        Toast.makeText(this, "extra data is $data1 & $data2", Toast.LENGTH_SHORT).show()
        Log.d("SecondActivity", "extra data is $data1 & $data2")

        binding.button2.setOnClickListener {
            testIntentReturnData()
        }
    }

    override fun onBackPressed() {
        testIntentReturnData()
    }

    private fun testIntentReturnData() {
        val intent = Intent()
        intent.putExtra("data_return", "Hello FirstActivity")
        setResult(RESULT_OK, intent)
        finish()
    }

    companion object {
        fun actionStart(context: Context, data1: String, data2: String) {
            val intent = Intent(context, SecondActivity::class.java)
            intent.putExtra("param1", data1)
            intent.putExtra("param2", data2)
            context.startActivity(intent)
        }
    }
}