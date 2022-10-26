package io.ymsoft.androidstudy.main

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import io.ymsoft.androidstudy.base.BaseActivity
import io.ymsoft.androidstudy.R
import io.ymsoft.androidstudy.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.Executors

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private var imageCapture: ImageCapture? = null

    private val cameraExecutor by lazy { Executors.newSingleThreadExecutor() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSION, 10)
        }

        binding.ivPreview.setOnClickListener {
            takePhoto()
        }

    }

    private val tempFile by lazy { File(cacheDir, "temp.jpg") }
    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        if (!tempFile.exists()) tempFile.createNewFile()
        val outputFileOptions = ImageCapture.OutputFileOptions.Builder(tempFile).build()

        imageCapture.takePicture(
            outputFileOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    outputFileResults.savedUri?.let {
                        setCropImage(it)
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    println("logp-[MainActivity][onError]: ${exception.message}")
                    exception.printStackTrace()
                }

            })
    }

    private fun setCropImage(uri: Uri) {
        lifecycleScope.launch {
            val bitmap = ImageCropUtil.centerCrop(
                tempFile,
                binding.layoutBox.width.toFloat() / binding.pv.width,
                binding.layoutBox.height.toFloat() / binding.pv.height
            )
            bitmap?.saveTo(tempFile.absolutePath)

            binding.ivPreview.setImageBitmap(bitmap)

        }

    }

    private fun startCamera() {
        val future = ProcessCameraProvider.getInstance(this)
        future.addListener({
            bindPreview(future.get())
        }, ContextCompat.getMainExecutor(this))
    }

    private fun bindPreview(provider: ProcessCameraProvider) {
        var preview = Preview.Builder().build()
        var cameraSelector =
            CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()
        imageCapture = ImageCapture.Builder().build()

        preview.setSurfaceProvider(binding.pv.surfaceProvider)

        try {
            provider.unbindAll()
            provider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
        } catch (e: Exception) {
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }


    fun allPermissionsGranted() = REQUIRED_PERMISSION.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private val REQUIRED_PERMISSION = mutableListOf(
            Manifest.permission.CAMERA
        ).apply {
            if (Build.VERSION.SDK_INT < -Build.VERSION_CODES.P) {
                add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }.toTypedArray()
    }
}

suspend fun Bitmap.saveTo(path: String, quality: Int = 100) = withContext(Dispatchers.IO) {
    val file = File(path)
    kotlin.runCatching {
        if (!file.exists()) {
            file.createNewFile()
        }
        val fos = FileOutputStream(path, false)
        val bos = BufferedOutputStream(fos)
        compress(Bitmap.CompressFormat.JPEG, quality, bos)
        bos.flush()
        bos.close()
        true
    }.getOrElse { false }
}