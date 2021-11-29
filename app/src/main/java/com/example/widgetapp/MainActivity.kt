package com.example.widgetapp

import android.content.Intent
import android.os.Bundle
import com.example.widgetapp.anim.LayoutTransitionActivity
import com.example.widgetapp.databinding.ActivityMainBinding
import com.example.widgetapp.view.SuccessViewActivity

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
    }

    override fun onInit(savedInstanceState: Bundle?) {
        setup()
    }
}