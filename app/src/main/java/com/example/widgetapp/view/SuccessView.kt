package com.example.widgetapp.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class SuccessView(context:Context,attrs:AttributeSet?):View(context, attrs) {
    constructor(context: Context):this(context,null)
    private var mAnimatedValue:Float = 0.0f
    private val mDistPath = Path()
    private var mOnDrawCheck = false

    init {
        val animator = ValueAnimator.ofFloat(0.0f,2.0f)
        animator.apply {
            addUpdateListener {
                mAnimatedValue = it.animatedValue as Float
                invalidate()
            }
            duration = 2000
        }
        animator.start()
    }
    private val mPath by lazy {
        Path().apply {
            addOval(RectF(20.0f,20.0f,200.0f,200.0f),Path.Direction.CW)
            val checkPath = Path().apply {
                moveTo(65.0f,115.0f)
                lineTo(115.0f,165.0f)
                lineTo(165.0f,65.0f)
            }
            addPath(checkPath)
        }
    }
    private val mPathMeasure by lazy {
        PathMeasure().apply {
            setPath(mPath,false)
        }
    }

    private val mPaint by lazy {
        Paint().apply {
            color = Color.BLUE
            strokeWidth = 20.0f
            style = Paint.Style.STROKE
            isAntiAlias = true
            strokeCap = Paint.Cap.ROUND
            pathEffect = CornerPathEffect(5.0f)
            maskFilter = BlurMaskFilter(20.0f,BlurMaskFilter.Blur.INNER)
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (mAnimatedValue <= 1.0f){
            if (mOnDrawCheck) mOnDrawCheck = false
            val stop = mPathMeasure.length * mAnimatedValue
            mPathMeasure.getSegment(0.0f,stop,mDistPath,true)
        }else{
            if (!mOnDrawCheck) {
                mOnDrawCheck = true
                mPathMeasure.nextContour()
            }
            val stop = mPathMeasure.length * (mAnimatedValue-1.0f)
            mPathMeasure.getSegment(0.0f,stop,mDistPath,true)
        }
        canvas.drawPath(mDistPath,mPaint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.makeMeasureSpec(240,MeasureSpec.EXACTLY)
        val height = MeasureSpec.makeMeasureSpec(240,MeasureSpec.EXACTLY)
        setMeasuredDimension(width,height)
    }
}