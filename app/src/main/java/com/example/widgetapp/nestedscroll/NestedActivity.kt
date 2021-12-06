package com.example.widgetapp.nestedscroll

import android.os.Bundle
import com.example.widgetapp.BaseActivity
import com.example.widgetapp.databinding.ActivityNestedBinding

class NestedActivity : BaseActivity<ActivityNestedBinding>() {
    override fun onInit(savedInstanceState: Bundle?) {
        val items = ArrayList<String>()
        for (i in 0 until 50){
            items.add("")
        }
        binding.rv.adapter = RvAdapter(items)
        binding.refreshBtn.setOnClickListener {
            binding.refreshView.setRefreshing(!binding.refreshView.isRefreshing())
        }
    }
}