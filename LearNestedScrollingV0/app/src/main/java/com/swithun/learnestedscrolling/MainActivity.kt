package com.swithun.learnestedscrolling

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import com.swithun.learnestedscrolling.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this)).also {
            setContentView(it.root)
//            it.parent.TAG = "parent"
//            it.child.TAG = "child"

            it.childClickTest.setOnClickListener {
                Toast.makeText(this, "child click", Toast.LENGTH_SHORT).show()
            }

            it.parentClickTest.setOnClickListener {
                Toast.makeText(this, "parent click", Toast.LENGTH_SHORT).show()
            }
        }
    }
}