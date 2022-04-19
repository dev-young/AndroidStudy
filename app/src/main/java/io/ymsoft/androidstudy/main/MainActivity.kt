package io.ymsoft.androidstudy.main

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import io.ymsoft.androidstudy.R
import io.ymsoft.androidstudy.base.BaseActivity
import io.ymsoft.androidstudy.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    //갤러리 요청시
    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            // Handle the returned Uri
            binding.iv.setImageURI(uri)
            binding.iv.isVisible = true
        }

    //일반적인 액티비티 호출시
    private val generalLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                it.data?.let { intent ->
                    //수신된 인텐트 처리리
                    Toast.makeText(
                        this,
                        "generalLauncher: ${intent.getIntExtra("return", 0)}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                //
            }
        }

    //커스텀 계약을 사용시
    private val customLauncher = registerForActivityResult(CustomContract()) {
        Toast.makeText(this, "customLauncher: $it", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.iv.setOnClickListener {
            binding.iv.isVisible = false
        }

        binding.btn1.setOnClickListener {
            getContent.launch("image/*")
            /*
            ActivityResultLauncher 는 onCreate() 가 끝나기 전에 초기화 되어야 한다.  리스너 내부나 다른 onCreate 이후 생명주기에서 초기화 된 경우 에러가 발생한다.
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                binding.iv.setImageURI(uri)
            }
            */
        }

        binding.btn2.setOnClickListener {
            generalLauncher.launch(Intent(this, SubActivity::class.java))
        }

        binding.btn3.setOnClickListener {
            customLauncher.launch(123)
        }


    }
}