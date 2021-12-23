package com.example.widgetapp.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.drawable.toBitmap
import kotlin.math.min

class CircleImageView(context: Context,attrs: AttributeSet?) : AppCompatImageView(context,attrs) {
    constructor(context: Context) : this(context,null)

    private val mBitmap by lazy { drawable.toBitmap() }

    private val mShader by lazy {
        val matrix = Matrix()
/*        if (mBitmap.width >= mBitmap.height){
            scaleHeight(matrix)
        }else{
            scaleWidth(matrix)
        }*/
        BitmapShader(mBitmap,Shader.TileMode.REPEAT,Shader.TileMode.REPEAT).apply {
            setLocalMatrix(matrix)
        }
    }
    private val mPaint by lazy {
        Paint().apply {
            shader = mShader
        }
    }

    private fun scaleWidth(m:Matrix){
        val dWidth = mBitmap.width
        if (dWidth >= width){
            m.setScale(width.toFloat() / dWidth,width.toFloat() / dWidth)
        }else{
            m.setScale(dWidth.toFloat() / width,dWidth.toFloat() / width)
        }
    }

    private fun scaleHeight(m:Matrix){
        val dHeight = mBitmap.height
        val factor = if (dHeight >= height){
            height.toFloat() / dHeight
        }else{
            dHeight.toFloat() / height
        }
        m.setScale(factor,factor)
    }

    override fun onDraw(canvas: Canvas) {
        val centerX = width / 2
        val centerY = height / 2
        val min = min(centerX,centerY)
        canvas.drawCircle(centerX.toFloat(),centerY.toFloat(),min.toFloat(),mPaint)
    }
}