package com.example.moire

import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import org.opencv.imgcodecs.Imgcodecs

fun removeMoire(inputPath: String, outputPath: String) {
    // 读取为灰度图
    val src = Imgcodecs.imread(inputPath, Imgcodecs.IMREAD_GRAYSCALE)
    src.convertTo(src, CvType.CV_32FC1)

    // 准备复数矩阵
    val planes = ArrayList<Mat>()
    planes.add(src)
    planes.add(Mat.zeros(src.size(), CvType.CV_32F))
    val complex = Mat()
    Core.merge(planes, complex)

    // 傅里叶变换
    Core.dft(complex, complex)

    // 移位频谱
    shiftDFT(complex)

    // 创建圆形低通滤波器掩模
    val mask = Mat.zeros(complex.size(), CvType.CV_32F)
    val center = Point(complex.cols() / 2.0, complex.rows() / 2.0)
    val radius = 30 // 调整半径以控制滤波强度
    Imgproc.circle(mask, center, radius, Scalar(1.0), -1)

    // 应用掩模（需合并为双通道）
    val maskPlanes = ArrayList<Mat>().apply {
        add(mask)
        add(mask)
    }
    Core.merge(maskPlanes, mask)
    Core.multiply(complex, mask, complex)

    // 逆移位
    shiftDFT(complex)

    // 逆傅里叶变换
    Core.idft(complex, complex, Core.DFT_SCALE or Core.DFT_REAL_OUTPUT)

    // 提取结果
    val filtered = Mat()
    Core.split(complex, planes)
    Core.normalize(planes[0], filtered, 0.0, 255.0, Core.NORM_MINMAX, CvType.CV_8U)

    Imgcodecs.imwrite(outputPath, filtered)
}

private fun shiftDFT(mat: Mat) {
    val cx = mat.cols() / 2
    val cy = mat.rows() / 2
    val q0 = Mat(mat, Rect(0, 0, cx, cy))
    val q1 = Mat(mat, Rect(cx, 0, cx, cy))
    val q2 = Mat(mat, Rect(0, cy, cx, cy))
    val q3 = Mat(mat, Rect(cx, cy, cx, cy))

    val tmp = Mat()
    q0.copyTo(tmp)
    q3.copyTo(q0)
    tmp.copyTo(q3)

    q1.copyTo(tmp)
    q2.copyTo(q1)
    tmp.copyTo(q2)
}