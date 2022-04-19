package io.ymsoft.androidstudy.main

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract


class CustomContract : ActivityResultContract<Int, Int?>() {
    override fun createIntent(context: Context, input: Int?) =
        Intent(context, SubActivity::class.java).apply {
            putExtra("init", input)
        }

    override fun parseResult(resultCode: Int, intent: Intent?): Int? {
        if (resultCode != RESULT_OK) return null
        return intent?.getIntExtra("return", 0)
    }
}