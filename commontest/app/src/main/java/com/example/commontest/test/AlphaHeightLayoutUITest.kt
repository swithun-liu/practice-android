package com.example.commontest.test

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.commontest.databinding.AlphaHeightTestLayoutBinding
import com.example.commontest.test.base.IUITest

class AlphaHeightLayoutUITest: IUITest {
    override fun getName(): String {
        return "AlphaHeight"
    }

    override fun getViewHolderAndView(context: Context, parent: ViewGroup): Pair<Any, View> {
        val binding = AlphaHeightTestLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        binding.visible.setOnClickListener {
            binding.content.toVisible()
        }

        binding.invisible.setOnClickListener {
            binding.content.toInvisible()
        }
        return binding to binding.root
    }

    override fun getCaseList(): Map<String, Any> {
        return mapOf(
            "1" to "1"
        )
    }

    override fun bindState(vh: Any, data: Any) {

    }
}