package com.example.moire

import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import org.opencv.imgcodecs.Imgcodecs

fun quickRemoveMoire(inputPath: String, outputPath: String) {
    val src = Imgcodecs.imread(inputPath)
    val dst = Mat()
    // 高斯模糊
    Imgproc.GaussianBlur(src, dst, Size(15.0, 15.0), 5.0)
    // 或中值滤波
//   Imgproc.medianBlur(src, dst, 9)
    Imgcodecs.imwrite(outputPath, dst)
}