package com.example.moire

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.opencv.android.OpenCVLoader
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    // 提前声明 launcher
    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            Log.d("PhotoPicker", "Selected URI: $uri")
        } else {
            Log.d("PhotoPicker", "No media selected")
        }


        uri?.let {
            // 将 Uri 转换为文件路径
            val inputPath = getRealPathFromUri(uri)
            // 生成输出路径（例如保存到应用缓存目录）
            val outputPath = File(cacheDir, "processed_image.jpg").absolutePath
            // 调用去摩尔纹方法（注意：需在后台线程执行！）
            Thread {
                removeMoire(inputPath!!, outputPath)
                // 处理完成后更新 UI（例如显示图片）
                runOnUiThread {
                    val bitmap = BitmapFactory.decodeFile(outputPath)
                    findViewById<ImageView>(R.id.result_image).setImageBitmap(bitmap)
                }
            }.start()
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        OpenCVLoader.initLocal()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        (findViewById<TextView>(R.id.btn)!!).setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }


    // 工具方法：将 Uri 转换为真实文件路径
    private fun getRealPathFromUri(uri: Uri): String? {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            val file = File.createTempFile("temp", ".jpg", cacheDir)
            FileOutputStream(file).use { output ->
                inputStream?.copyTo(output)
            }
            file.absolutePath
        } catch (e: Exception) {
            null
        }
    }
}