package com.example.doubanmovie.service

import android.app.DownloadManager
import android.app.DownloadManager.Request
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Messenger
import android.util.Log
import android.widget.Toast


private const val MSG_SAY_HELLO = 1
private const val MSG_DOWNLOAD_COVER = 2


class DownloadService : Service() {

    private val tag = "DownloadService"

    private lateinit var mMessenger: Messenger

    internal class IncomingHandler(private val context: Context, private val applicationContext: Context = context.applicationContext) : Handler() {

        private val tag = "DownloadService"

        override fun handleMessage(msg: Message) {
            when(msg.what) {
                MSG_SAY_HELLO ->
                    Toast.makeText(applicationContext, "Hello!", Toast.LENGTH_SHORT).show()
                MSG_DOWNLOAD_COVER -> {
                    val coverUrl = msg.data.getString("coverUrl")
                    val movieName = msg.data.getString("movieName")
                    val fileName = " $movieName--${coverUrl?.substringAfterLast('/')}"

                    Toast.makeText(applicationContext, "下载 -- $fileName", Toast.LENGTH_SHORT).show()

                    val downloadManger = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                    val resource = Uri.parse(coverUrl)
                    Log.d(tag, "start Download movieCover -- $fileName -- from $coverUrl")
                    val request = Request(resource)
                    request.setDestinationInExternalPublicDir("Download", fileName);
                    downloadManger.enqueue(request)
                }
                else -> super.handleMessage(msg)
            }
        }
    }

    override fun onBind(intent: Intent): IBinder {
        Toast.makeText(applicationContext, "binding", Toast.LENGTH_SHORT).show()
        mMessenger = Messenger(IncomingHandler(this))
        return mMessenger.binder
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(tag, "onCreate executed")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("DownloadService", "onStartCommand executed")
        return super.onStartCommand(intent, flags, startId)
    }
}