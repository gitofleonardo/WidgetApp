package com.example.widgetapp

import android.content.Intent
import android.os.Bundle
import com.example.widgetapp.anim.AnimatorVectorActivity
import com.example.widgetapp.anim.LayoutTransitionActivity
import com.example.widgetapp.databinding.ActivityMainBinding
import com.example.widgetapp.nestedscroll.NestedActivity
import com.example.widgetapp.threed.CameraImageActivity
import com.example.widgetapp.view.SuccessViewActivity
import com.example.widgetapp.view.TextBannerActivity

class MainActivity : BaseActivity<ActivityMainBinding>() {
    private fun setup(){
        binding.layoutTransition.setOnClickListener {
            val intent = Intent(this,LayoutTransitionActivity::class.java)
            startActivity(intent)
        }
        binding.successView.setOnClickListener {
            val intent = Intent(this,SuccessViewActivity::class.java)
            startActivity(intent)
        }
        binding.animatedDrawable.setOnClickListener {
            val intent = Intent(this,AnimatorVectorActivity::class.java)
            startActivity(intent)
        }
        binding.cameraImageView.setOnClickListener {
            val intent = Intent(this,CameraImageActivity::class.java)
            startActivity(intent)
        }
        binding.banner.setOnClickListener {
            val intent = Intent(this,TextBannerActivity::class.java)
            startActivity(intent)
        }
        binding.nestedScroll.setOnClickListener {
            val intent = Intent(this,NestedActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onInit(savedInstanceState: Bundle?) {
        setup()
    }
}