package io.ymsoft.androidstudy.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import io.ymsoft.androidstudy.R
import io.ymsoft.androidstudy.base.BaseActivity
import io.ymsoft.androidstudy.databinding.ActivitySubBinding

class SubActivity : BaseActivity<ActivitySubBinding>(R.layout.activity_sub) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val init = intent.getIntExtra("init", 0).toString()
        binding.edit.setText(init)

        binding.btn.setOnClickListener {

            setResult(Activity.RESULT_OK, Intent().apply {
                val v = binding.edit.text.toString().toInt()
                putExtra("return", v)
            })
            finish()
        }
    }
}