package com.example.broadcasttest

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.broadcasttest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var timeChangeReceiver: TimeChangeReceiver
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 动态注册接受Time_tick的BroadcastReceiver
        val intentFilter = IntentFilter()
        intentFilter.addAction("android.intent.action.TIME_TICK")
        timeChangeReceiver = TimeChangeReceiver()
        registerReceiver(timeChangeReceiver, intentFilter)

        binding.button.setOnClickListener {
            val intent = Intent("com.example.broadcasttest.MY_BROADCAST")
            /** 静态注册的BroadcastReceiver是无法接收隐式广播的，而默认情况下我们发出的自定义广播恰恰都是隐式广播。
             * 因此这里一定要调用setPackage()方法，指定这条广播是发送给哪个应用程序的，从而让它变成一条显式广播 **/
            intent.setPackage(packageName)
            /** 发送标准广播 **/
//            sendBroadcast(intent)
            /** 发送有序广播 **/
            sendOrderedBroadcast(intent, null)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(timeChangeReceiver)
    }

    inner class TimeChangeReceiver : BroadcastReceiver() {

        override fun onReceive(p0: Context?, p1: Intent?) {
            Toast.makeText(p0, "Time has changed", Toast.LENGTH_SHORT).show()
        }

    }
}