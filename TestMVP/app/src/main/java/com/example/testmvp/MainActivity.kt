package com.example.testmvp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.Data
import com.example.testmvp.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Date

class MainActivity : AppCompatActivity(), MainContract.View {
    private lateinit var binding: ActivityMainBinding
    private val mPresenter by lazy { MainPresenter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        mPresenter.loadData()
        binding.mButton.setOnClickListener {
            mPresenter.functionA()
        }
    }

    override fun onDataLoaded(s: String) {
        binding.mTextView.text = s
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onFunctionAFinished() {
        binding.mTextView.text = "我被更新了 + ${SimpleDateFormat("dd/M/yyyy hh:mm:ss").format(Date())}"
    }
}