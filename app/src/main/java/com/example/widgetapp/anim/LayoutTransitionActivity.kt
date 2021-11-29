package com.example.widgetapp.anim

import android.animation.LayoutTransition
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.vectordrawable.graphics.drawable.AnimationUtilsCompat
import com.example.widgetapp.BaseActivity
import com.example.widgetapp.R
import com.example.widgetapp.databinding.ActivityLayoutTransitionBinding

class LayoutTransitionActivity : BaseActivity<ActivityLayoutTransitionBinding>() {
    override fun onInit(savedInstanceState: Bundle?) {
        binding.container.apply {
            layoutTransition = LayoutTransition().apply {
                val propScaleX = PropertyValuesHolder.ofFloat("scaleX",1.2f,1.0f)
                val propScaleY = PropertyValuesHolder.ofFloat("scaleY",1.2f,1.0f)
                val propAlpha = PropertyValuesHolder.ofFloat("alpha",0.0f,1.0f)

                val propScaleXExit = PropertyValuesHolder.ofFloat("scaleX",1.0f,0.8f)
                val propScaleYExit = PropertyValuesHolder.ofFloat("scaleY",1.0f,0.8f)
                val propAlphaExit = PropertyValuesHolder.ofFloat("alpha",1.0f,0.0f)

                setAnimator(LayoutTransition.APPEARING,ObjectAnimator.ofPropertyValuesHolder(binding.container,propScaleX,propScaleY,propAlpha).apply { duration = 300 })
                setAnimator(LayoutTransition.DISAPPEARING,ObjectAnimator.ofPropertyValuesHolder(binding.container,propScaleXExit,propScaleYExit,propAlphaExit).apply { duration = 300 })
            }
        }
        binding.btn.setOnClickListener {
            val view = layoutInflater.inflate(R.layout.layout_simple_text,null,false)
            binding.container.addView(view)
        }
        binding.btnDelete.setOnClickListener {
            if (binding.container.childCount <= 0){
                return@setOnClickListener
            }
            binding.container.removeViewAt(0)
        }
    }
}