package com.example.widgetapp.draw

import android.os.Bundle
import com.example.widgetapp.BaseActivity
import com.example.widgetapp.databinding.ActivityCanvasBinding
import com.example.widgetapp.view.CanvasMode

class CanvasActivity : BaseActivity<ActivityCanvasBinding>() {
    override fun onInit(savedInstanceState: Bundle?) {
        binding.clearModeEnabled.setOnCheckedChangeListener { _, isChecked -> binding.canvas.canvasMode = if (isChecked) CanvasMode.CLEAR else CanvasMode.DRAW }
    }
}