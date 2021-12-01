package com.example.widgetapp.anim

import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import com.example.widgetapp.BaseActivity
import com.example.widgetapp.databinding.ActivityAnimatorVectorBinding

class AnimatorVectorActivity : BaseActivity<ActivityAnimatorVectorBinding>() {
    override fun onInit(savedInstanceState: Bundle?) {
        binding.img.setOnClickListener {
            val d = binding.img.drawable as AnimatedVectorDrawable
            d.start()
        }
    }
}