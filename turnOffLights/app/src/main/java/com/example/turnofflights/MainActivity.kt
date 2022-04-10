package com.example.turnofflights

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import com.example.turnofflights.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var bluetoothAdapter: BluetoothAdapter
    private var blueList = mutableListOf<BluetoothDevice>()
    private var bluetoothDevice: BluetoothDevice? = null
    private lateinit var binding: ActivityMainBinding
    private var btSocket: BluetoothSocket? = null
    private val count = -1
    private var listData = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bluetoothAdapter =
            (this.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter

        binding.btn1.setOnClickListener {

        }

        isBluetoothAdapterEnable()
        isBluetoothOn()
        hasGottenBluetoothDevicesList()
        printList()

    }

    fun isBluetoothAdapterEnable(): Boolean {
        return when (bluetoothAdapter != null) {
            true -> {
                binding.tvTint.text = "bluetooth is usable"
                true
            }
            false -> {
                binding.tvTint.text = "bluetooth is unusable"
                false
            }
        }
    }


    fun isBluetoothOn(): Boolean {
        return when (!bluetoothAdapter.isEnabled) {
            true -> {
                binding.tvTint.text = "bluetooth is off, please open bluetooth"
                false
            }
            false -> {
                binding.tvTint.text = "bluetooth is on"
                true
            }
        }
    }

    fun hasGottenBluetoothDevicesList(): Boolean {
        blueList = getBoundDevicesList()
        return (blueList.size > 0).also {
            Log.d("swithun-xxxx",  "hasGottenBluetoothDeviceList: $it")
        }
    }

    @SuppressLint("MissingPermission")
    fun getBoundDevicesList(): MutableList<BluetoothDevice> {
        return bluetoothAdapter.bondedDevices.toMutableList()
    }

    @SuppressLint("MissingPermission")
    fun printList() {

        bluetoothDevice = null
        listData.clear()
        blueList.forEach {
            val name = "${it.address} : ${it.name}"
            listData.add(it.name)
            Log.d("swithun-xxxx", "printList - ${it.name}")
        }
        val adapter: ArrayAdapter<String> =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, listData)
        binding.lvBluetoothDevicesList.adapter = adapter
    }

}