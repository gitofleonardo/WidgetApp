package com.example.widgetapp.nestedscroll

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.OverScroller
import android.widget.Scroller
import androidx.core.view.NestedScrollingChild3
import androidx.core.view.NestedScrollingChildHelper
import androidx.core.view.NestedScrollingParent
import androidx.core.view.ViewCompat
import kotlin.math.abs
import kotlin.math.min

private const val TAG = "HNestedScrollView"

class HNestedScrollView(context: Context,attrs:AttributeSet?,defStyleRes:Int) : ViewGroup(context, attrs,defStyleRes), NestedScrollingChild3, GestureDetector.OnGestureListener {
    constructor(context: Context,attrs: AttributeSet?):this(context,attrs,-1)
    constructor(context: Context):this(context,null)

    private val mNestedHelper by lazy {
        NestedScrollingChildHelper(this).apply {
            isNestedScrollingEnabled = true
        }
    }
    private val mDetector by lazy { GestureDetector(context,this) }
    private val mScroller by lazy {
        OverScroller(context)
    }
    private var mOffsetTop = 0
    private var mFling = false
    private val mScrollContent:View?
        get() {
            if (childCount <= 0){
                return null
            }
            return getChildAt(0)
        }
    private val mScrollableContentHeight:Int
        get() {
            val contentHeight = mScrollContent?.measuredHeight?:0
            return if (contentHeight > measuredHeight){
                contentHeight - measuredHeight
            }else{
                0
            }
        }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        mScrollContent?.let {
            it.layout(0,0,it.measuredWidth,it.measuredHeight)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        val parentSpec = MeasureSpec.makeMeasureSpec(height,MeasureSpec.UNSPECIFIED)
        mScrollContent?.let {
            measureChild(it,widthMeasureSpec,parentSpec)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null){
            return super.onTouchEvent(event)
        }
        when (event.action){
            MotionEvent.ACTION_DOWN -> {
                mScroller.forceFinished(true)
                mDetector.onTouchEvent(event)
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL,ViewCompat.TYPE_TOUCH)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                parent.requestDisallowInterceptTouchEvent(true)
                mDetector.onTouchEvent(event)
                return true
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                parent.requestDisallowInterceptTouchEvent(false)
                mDetector.onTouchEvent(event)
                stopNestedScroll(ViewCompat.TYPE_TOUCH)
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    override fun startNestedScroll(axes: Int, type: Int): Boolean {
        return mNestedHelper.startNestedScroll(axes, type)
    }

    override fun stopNestedScroll(type: Int) {
        mNestedHelper.stopNestedScroll(type)
    }

    override fun hasNestedScrollingParent(type: Int): Boolean {
        return parent is NestedScrollingParent
    }

    override fun dispatchNestedScroll(
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        offsetInWindow: IntArray?,
        type: Int,
        consumed: IntArray
    ) {
        mNestedHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow, type, consumed)
    }

    override fun dispatchNestedScroll(
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        offsetInWindow: IntArray?,
        type: Int
    ): Boolean {
        return mNestedHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow, type)
    }

    override fun dispatchNestedPreScroll(
        dx: Int,
        dy: Int,
        consumed: IntArray?,
        offsetInWindow: IntArray?,
        type: Int
    ): Boolean {
        return mNestedHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, type)
    }

    override fun onDown(e: MotionEvent?): Boolean {
        return true
    }

    override fun onShowPress(e: MotionEvent?) {

    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        return false
    }

    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent?,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        if (distanceY == 0.0f){
            return true
        }
        var dx = distanceX.toInt()
        var dy = distanceY.toInt()
        var shouldScroll = 0
        val consumed = IntArray(2){ 0 }
        val offsetInWindow = IntArray(2){ 0 }
        //pre scroll
        dispatchNestedPreScroll(dx,dy,consumed,offsetInWindow,ViewCompat.TYPE_TOUCH)
        dx -= consumed[0]
        dy -= consumed[1]
        //scroll up
        if (distanceY > 0){
            if (abs(mOffsetTop) < mScrollableContentHeight){
                //scroll not arrived bottom yet, continuing scrolling
                shouldScroll = min(mScrollableContentHeight - abs(mOffsetTop),dy)
            }
        }else{
            //scroll down
            if (abs(mOffsetTop) > 0){
                shouldScroll = -min(abs(mOffsetTop), abs(dy))
            }
        }
        scrollBy(0,shouldScroll)
        dispatchNestedScroll(0,shouldScroll,dx,dy - shouldScroll,offsetInWindow,ViewCompat.TYPE_TOUCH,consumed)
        mOffsetTop -= shouldScroll
        return true
    }

    override fun onLongPress(e: MotionEvent?) {

    }

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        val vx = velocityX.toInt()
        val vy = velocityY.toInt()
        val parentConsumed = mNestedHelper.dispatchNestedPreFling(velocityX, velocityY)
        if (!parentConsumed){
            mScroller.forceFinished(false)
            mScroller.fling(0,mOffsetTop,vx,vy,0,0,-mScrollableContentHeight,0)
            mFling = true
            invalidate()
        }
        return true
    }

    override fun computeScroll() {
        if (mScroller.computeScrollOffset()){
            val offset = mOffsetTop - mScroller.currY
            mOffsetTop = mScroller.currY
            scrollBy(0,offset)
            invalidate()
        }else if (mFling){
            mFling = false
            Log.e(TAG,"Stop fling, velocity:${mScroller.currVelocity}")
            mNestedHelper.dispatchNestedFling(mScroller.currVelocity,mScroller.currVelocity,false)
        }
    }
}