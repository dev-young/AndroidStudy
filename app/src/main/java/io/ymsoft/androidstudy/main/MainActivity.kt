package io.ymsoft.androidstudy.main

import android.os.Bundle
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import io.ymsoft.androidstudy.base.BaseActivity
import io.ymsoft.androidstudy.R
import io.ymsoft.androidstudy.databinding.ActivityMainBinding

/**
 * SnapHelper 를 사용해 특정 버튼을 가운데로 포커싱
 * */
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initRv(binding.rv1, listOf("1111", "222222","1111",  "3333"))
        initRv(binding.rv2, listOf("동영상", "사진", "222222","1111", "222222","1111", "222222","1111", "222222","1111", "222222","1111", "222222", "3333333333"))
        initRv(binding.rv3, listOf("1111", "2222","증명사진"))

    }

    fun initRv(recyclerView: RecyclerView, items: List<String>){
        val snapHelper = LinearSnapHelper()

        val adapter = SelectWithScrollAdapter(recyclerView) {
            smoothSnapScrollToPosition(recyclerView, snapHelper, it)
        }.apply {
            itemList.addAll(items)
        }
        snapHelper.attachToRecyclerView(recyclerView)

        recyclerView.apply {
            this.adapter = adapter
            post {
                val padding = measuredWidth / 2
                updatePadding(left = padding, right = padding)
            }
            postDelayed({smoothSnapScrollToPosition(recyclerView, snapHelper, 0)}, 10)
        }
    }

    private fun smoothSnapScrollToPosition(rv: RecyclerView, snapHelper: SnapHelper, it: Int) {
        rv.apply {
            val view = layoutManager?.findViewByPosition(it)
            if (view == null) {
                return@apply
            }

            val snapDistance =
                snapHelper.calculateDistanceToFinalSnap(layoutManager!!, view) ?: return@apply
            if (snapDistance[0] != 0 || snapDistance[1] != 0) {
                smoothScrollBy(snapDistance[0], snapDistance[1])
            }
        }
    }
}