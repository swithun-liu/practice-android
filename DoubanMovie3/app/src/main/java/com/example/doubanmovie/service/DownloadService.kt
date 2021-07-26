package com.example.doubanmovie.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.doubanmovie.R
import com.example.doubanmovie.ui.main.view.MainActivity

class DownloadService : Service() {

    private val tag = "DownloadService"
    private val downloadBinder = DownloadBinder()

    class DownloadBinder : Binder() {

        private val tag = "DownloadService"

        fun startDownload(movieId: Int) {
            Log.d(tag, "start Download $movieId")
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
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            val channel = NotificationChannel("download_service", "前台Service通知", NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
        }
        val intent = Intent(this, MainActivity::class.java)
        val pi = PendingIntent.getActivity(this, 0, intent, 0)
        val notification = NotificationCompat.Builder(this, "download_service")
            .setContentTitle("This is content title")
            .setContentText("This is content text")
            .setSmallIcon(R.drawable.small_icon)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.large_icon))
            .setContentIntent(pi)
            .build()
        startForeground(1, notification)
        Log.d(tag, "startForeground")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("DownloadService", "onStartCommand executed")
        return super.onStartCommand(intent, flags, startId)
    }
}