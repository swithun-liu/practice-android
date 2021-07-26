package com.example.doubanmovie.service

import android.app.DownloadManager
import android.app.DownloadManager.Request
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import android.util.Log

class DownloadService : Service() {

    private val tag = "DownloadService"
    private val downloadBinder = DownloadBinder()

    inner class DownloadBinder : Binder() {

        private val tag = "DownloadService"

        fun downloadCover(coverUrl: String, movieName: String) {

            val downloadManger = this@DownloadService.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val resource = Uri.parse(coverUrl)
            val fileName = " $movieName--${coverUrl.substringAfterLast('/')}"
            Log.d(tag, "start Download movieCover -- $fileName -- from $coverUrl")
            val request = Request(resource)
            request.setDestinationInExternalPublicDir("Download", fileName);
            downloadManger.enqueue(request)
        }

        fun getProgress(): Int {
            Log.d(tag, "getProgress")
            return 0
        }
    }

    override fun onBind(intent: Intent): IBinder {
        return downloadBinder
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