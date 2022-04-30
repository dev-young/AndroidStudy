package io.ymsoft.androidstudy.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import io.ymsoft.androidstudy.R
import io.ymsoft.androidstudy.base.BaseActivity
import io.ymsoft.androidstudy.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.vm = viewModel
        binding.lifecycleOwner = this
    }
}