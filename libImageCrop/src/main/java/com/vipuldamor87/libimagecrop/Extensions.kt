package com.vipuldamor87.libimagecrop

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.DecimalFormat


private val format: DecimalFormat = DecimalFormat("#.##")
private const val MiB = 1024 * 1024.toLong()
private const val KiB: Long = 1024

fun File?.checkFileSize(): String? {
    val length = this?.length()!!.toDouble()
    if (length > MiB) {
        return format.format(length / MiB).toString() + " MB"
    }
    return if (length > KiB) {
        format.format(length / KiB).toString() + " KB"
    } else format.format(length).toString() + " Byte"
}


fun Uri.getURI(context: Context?, fileSize: Int): Bitmap {
    val bitmap = MediaStore.Images.Media.getBitmap(context!!.contentResolver, this)
    return getResizedBitmap(bitmap, fileSize)
}

fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap {
    var width = image.width
    var height = image.height

    val bitmapRatio = width.toFloat() / height.toFloat()
    if (bitmapRatio > 0) {
        width = maxSize
        height = (width / bitmapRatio).toInt()
    } else {
        height = maxSize
        width = (height * bitmapRatio).toInt()
    }
    return Bitmap.createScaledBitmap(image, width, height, true)
}

fun Bitmap.onConvertBitmapToFile(): File {
    val imgFile = File(Environment.getExternalStorageDirectory(), System.currentTimeMillis().toString() + ".jpg")
    val bytes = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
    val fo: FileOutputStream
    try {
        imgFile.createNewFile()
        fo = FileOutputStream(imgFile)
        fo.write(bytes.toByteArray())
        fo.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return imgFile
}