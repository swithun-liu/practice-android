package com.swithun.textsmb

import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.hierynomus.smbj.SMBClient
import com.hierynomus.smbj.auth.AuthenticationContext
import com.hierynomus.smbj.share.DiskShare
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lifecycleScope.launch(Dispatchers.IO) {
            testSMB()
        }
    }

    fun testSMB() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            val policy = ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }


        run {
            val client = SMBClient()
            client.connect("192.168.31.36")?.let { connection ->
                val ac = AuthenticationContext("Guest", CharArray(0), "")
                connection.authenticate(ac)?.let { session ->
                    session.connectShare("share")?.let { share ->
                        (share as DiskShare)?.let { diskShare ->
                            diskShare.list("").forEach {
                                Log.d("swithun-xxxx", "folder: ${it.shortName}")
                            }
                        }
                    }
                }
            }
        }
    }
}