package com.example.widgetapp.threed

import android.os.Bundle
import android.widget.TextView
import com.example.widgetapp.BaseActivity
import com.example.widgetapp.databinding.ActivityCameraImageBinding

class CameraImageActivity : BaseActivity<ActivityCameraImageBinding>() {
    override fun onInit(savedInstanceState: Bundle?) {
        binding.image.startAnimation()
    }
}