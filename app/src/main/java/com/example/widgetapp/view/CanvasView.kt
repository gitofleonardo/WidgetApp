package com.example.widgetapp.view

import android.content.Context
import android.graphics.*
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
    private val mClearPath = Path()
    private val mPaint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 5.0f
        style = Paint.Style.STROKE
    }
    private val mClearPathPaint = Paint().apply {
        color = Color.GRAY
        strokeWidth = 5.0f
        style = Paint.Style.STROKE
    }
    var canvasMode = CanvasMode.DRAW
    private var mPrevX = 0.0f
    private var mPrevY = 0.0f
    private var mShouldNextQuad = false

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val path = when (canvasMode) {
            CanvasMode.DRAW -> mPath
            CanvasMode.CLEAR -> mClearPath
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mShouldNextQuad = false
                mPrevX = event.x
                mPrevY = event.y
                path.moveTo(mPrevX,mPrevY)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                return if (!mShouldNextQuad){
                    mPrevX = event.x
                    mPrevY = event.y
                    mShouldNextQuad = true
                    true
                }else{
                    mShouldNextQuad = false
                    path.quadTo(mPrevX,mPrevY,event.x,event.y)
                    mPrevX= event.x
                    mPrevY = event.y
                    invalidate()
                    true
                }
            }
            MotionEvent.ACTION_UP,MotionEvent.ACTION_CANCEL -> {
                if (canvasMode == CanvasMode.CLEAR){
                    mClearPath.close()
                    mPath.op(mClearPath,Path.Op.DIFFERENCE)
                    mClearPath.reset()
                    invalidate()
                }
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawPath(mPath,mPaint)
        if (canvasMode == CanvasMode.CLEAR){
            canvas.drawPath(mClearPath,mClearPathPaint)
        }
    }

    fun clearCanvas(){
        mPath.reset()
        postInvalidate()
    }
}