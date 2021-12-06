package com.example.widgetapp.nestedscroll

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.example.widgetapp.R

class DefaultRefreshHeader(context: Context,attrs:AttributeSet?) : FrameLayout(context, attrs){
    constructor(context: Context):this(context,null)

    init {
        inflate(context,R.layout.layout_default_refresh_header,this)
    }
}