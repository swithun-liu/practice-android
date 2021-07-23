package com.example.testmvvm.ui.v

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.edit
import androidx.lifecycle.ViewModelProvider
import com.example.testmvvm.databinding.ActivityMainBinding
import com.example.testmvvm.ui.util.MainViewModelFactory
import com.example.testmvvm.ui.util.MyObserver
import com.example.testmvvm.ui.vm.MainViewModel

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel //1
    private lateinit var binding: ActivityMainBinding
    lateinit var sp: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        sp = getPreferences(Context.MODE_PRIVATE)
        val countReserved = sp.getInt("count_reserved", 0)
        setContentView(view)
        // lifeCycle测试
        lifecycle.addObserver(MyObserver())

        // ViewModel测试
        viewModel = ViewModelProvider(this, MainViewModelFactory(countReserved)).get(MainViewModel::class.java)
        binding.plusOneBtn.setOnClickListener {
            viewModel.plusOne()
        }
        binding.clearBtn.setOnClickListener {
            viewModel.clear()
        }
//        viewModel.counter.observe(this, Observer { count ->
//            binding.infoText.text = count.toString()
//        })
        viewModel.counter.observe(this) {
            binding.infoText.text = it.toString()
        }

    }

    override fun onPause() {
        super.onPause()
        sp.edit {
            putInt("count_reserved", viewModel.counter.value ?: 0)
        }
    }
}