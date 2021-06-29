package com.swithun.myapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.swithun.myapplication.databinding.FirstLayoutBinding

class FirstActivity : BaseActivity() {

    private lateinit var binding: FirstLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FirstLayoutBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.buttonExplicitIntent.setOnClickListener {
            Toast.makeText(this, "Explicit intent", Toast.LENGTH_SHORT).show()
            /**
             * 1. 显式Intent -> SecondActivity
             */
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }
        binding.buttonImplicitIntentActionDefultCategory.setOnClickListener {
            Toast.makeText(this, "Implicit intent (MyAction + DefaultCategory)", Toast.LENGTH_SHORT)
                .show()
            /**
             * 2. 隐式Intent -> SecondActivity ( MyAction + DefaultCategory )
             */
            val intent = Intent("com.swithun.myapplication.ACTION_START")
            startActivity(intent)
        }
        binding.buttonImplicitIntentActionCategoy.setOnClickListener {
            Toast.makeText(this, "Implicit intent (MyAction + MyCatetory)", Toast.LENGTH_SHORT)
                .show()
            /**
             * 2. 隐式Intent -> SecondActivity ( MyAction + DefaultCategory )
             */
            val intent = Intent("com.swithun.myapplication.ACTION_START")
            intent.addCategory("com.swithun.myapplication.MY_CATEGORY")
            startActivity(intent)
        }
        binding.buttonOpenLink.setOnClickListener {
            /**
             * 打开链接
             */
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://www.baidu.com")
            startActivity(intent)
        }
        binding.buttonIntentWithInfo.setOnClickListener {
            /**
             * 传递数据给下一个 Activity
             */
            val data = "Hello SecondActivity"
            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra("extra_data", data)
            startActivity(intent)
        }
        binding.buttonGetReturnedInfo.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startActivityForResult(intent, 1)
        }
        binding.buttonDestroyActivity.setOnClickListener {
            /**
             * 销毁 Activity
             */
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_item -> Toast.makeText(this, "You clickd Add", Toast.LENGTH_SHORT).show()
            R.id.remove_item -> Toast.makeText(this, "You clicked Remove", Toast.LENGTH_SHORT)
                .show()
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("FirstActivity", "onActivityResult - requestCode:$requestCode - resultCode:$resultCode - data:$data")
        when (requestCode) {
            1 -> if (resultCode == RESULT_OK) {
                val returnedData = data?.getStringExtra("data_return")
                Toast.makeText(this, "FirstActivity: returned data is $returnedData", Toast.LENGTH_SHORT).show()
                Log.d("FirstActivity", "returned data is $returnedData")
            }
        }
    }
}