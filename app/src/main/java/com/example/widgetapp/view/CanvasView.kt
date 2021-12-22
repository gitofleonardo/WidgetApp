package com.example.widgetapp.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

enum class CanvasMode {
    DRAW,
    CLEAR
}

class CanvasView(context:Context,attrs:AttributeSet?) : View(context, attrs) {
    constructor(context: Context):this(context,null)

    private val mPath = Path()
    private val mPaint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 5.0f
        style = Paint.Style.STROKE
    }
    private var mMode = CanvasMode.DRAW
    private var mPrevX = 0.0f
    private var mPrevY = 0.0f

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mPrevX = event.x
                mPrevY = event.y
                mPath.moveTo(mPrevX,mPrevY)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                val endX = (mPrevX + event.x) / 2
                val endY = (mPrevY + event.y) / 2
                mPath.quadTo(event.x,event.y,endX,endY)
                mPrevX= event.x
                mPrevY = event.y
                invalidate()
                return true
            }
            MotionEvent.ACTION_UP,MotionEvent.ACTION_CANCEL -> {
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawPath(mPath,mPaint)
    }

    fun clearCanvas(){
        mPath.reset()
        postInvalidate()
    }
}