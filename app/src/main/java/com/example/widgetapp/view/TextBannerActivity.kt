package com.example.widgetapp.view

import android.os.Bundle
import com.example.widgetapp.BaseActivity
import com.example.widgetapp.databinding.ActivityTextBannerBinding

class TextBannerActivity : BaseActivity<ActivityTextBannerBinding>() {
    private val mTexts by lazy { mutableListOf("Hello","World","And","Thank","You") }
    override fun onInit(savedInstanceState: Bundle?) {
        binding.banner.startScroll(mTexts,500,2000)
    }

    override fun onDestroy() {
        binding.banner.stopScroll()
        super.onDestroy()
    }
}