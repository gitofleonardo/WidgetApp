package com.example.widgetapp.threed

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import com.example.widgetapp.R

class CameraImageView(context: Context,attrs:AttributeSet?,defStyleAttr:Int):androidx.appcompat.widget.AppCompatImageView(context, attrs, defStyleAttr){
    constructor(context: Context,attrs: AttributeSet?):this(context,attrs,-1)
    constructor(context: Context):this(context,null)

    private val mBitmap:Bitmap = BitmapFactory.decodeResource(resources, R.mipmap.image)

    private val mPaint by lazy {
        Paint().apply {
            isAntiAlias = true
        }
    }
    private val mCamera by lazy { Camera() }
    private val mMatrix by lazy { Matrix() }
    private var mProgress = 0.0f
        set(value) {
            field = value
            invalidate()
        }
    private val mAnimator by lazy {
        ValueAnimator.ofFloat(0.0f,360.0f).apply {
            repeatCount = ValueAnimator.INFINITE
            duration = 2000
            addUpdateListener {
                val value = it.animatedValue as Float
                mProgress = value
            }
        }
    }

    fun startAnimation(){
        mAnimator.start()
    }

    override fun onDraw(canvas: Canvas) {
        mCamera.save()
        canvas.save()
        mPaint.alpha = 100
        canvas.drawBitmap(mBitmap,0.0f,0.0f,mPaint)

        mCamera.rotateX(mProgress)
        mCamera.getMatrix(mMatrix)
        val centerX = width / 2.0f
        val centerY = height / 2.0f
        mMatrix.preTranslate(-centerX,-centerY)
        mMatrix.postTranslate(centerX,centerY)
        canvas.setMatrix(mMatrix)
        //mCamera.applyToCanvas(canvas)
        mCamera.restore()
        super.onDraw(canvas)
        canvas.restore()
    }
}