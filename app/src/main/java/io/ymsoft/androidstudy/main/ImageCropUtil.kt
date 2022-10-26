package io.ymsoft.androidstudy.main

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

object ImageCropUtil {

    suspend fun centerCrop(resolver: ContentResolver, uri: Uri, widthPercent: Float, heightPercent: Float) = withContext(Dispatchers.IO) {
        val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(ImageDecoder.createSource(resolver, uri))
        } else {
            MediaStore.Images.Media.getBitmap(resolver, uri)
        }
        centerCrop(bitmap, widthPercent, heightPercent)
    }

    suspend fun centerCrop(file: File, widthPercent: Float, heightPercent: Float) = withContext(Dispatchers.IO) {
        centerCrop(BitmapFactory.decodeFile(file.absolutePath), widthPercent, heightPercent)
    }

    suspend fun centerCrop(bitmap: Bitmap, widthPercent: Float, heightPercent: Float): Bitmap? {
        val width = bitmap.width * widthPercent
        val height = bitmap.height * heightPercent
        return centerCrop(bitmap, width.toInt(), height.toInt())
    }

    suspend fun centerCrop(bitmap: Bitmap, width: Int, height: Int): Bitmap? {
        val left = (bitmap.width - width) / 2
        val top = (bitmap.height - height) / 2
        return crop(bitmap, left, top, width, height)
    }

    suspend fun crop(bitmap: Bitmap, rect: Rect): Bitmap? {
        return crop(bitmap, rect.left, rect.top, rect.width(), rect.height())
    }

    suspend fun crop(bitmap: Bitmap, _left: Int, _top: Int, _width: Int, _height: Int) = withContext(Dispatchers.Default) {
        val left = if (_left < 0) 0 else _left
        val top = if (_top < 0) 0 else _top
        val width = if (bitmap.width < _width) bitmap.width else _width
        val height = if (bitmap.height < _height) bitmap.height else _height
        kotlin.runCatching {
            Bitmap.createBitmap(bitmap, left, top, width, height)
        }.onFailure {
            it.printStackTrace()
        }.getOrNull()
    }
}
/**exifPath 를 통해 비트맵을 회전 시킨다.*/
fun Bitmap.applyOrientation(exifPath: String): Bitmap {
    val exif = ExifInterface(exifPath)
    val orientation =
        exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)
    val matrix = Matrix()

    when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> {
            matrix.postRotate(90f)
        }
        ExifInterface.ORIENTATION_ROTATE_180 -> {
            matrix.postRotate(180f)
        }
        ExifInterface.ORIENTATION_ROTATE_270 -> {
            matrix.postRotate(270f)
        }
        else -> {
            return this
        }
    }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}
