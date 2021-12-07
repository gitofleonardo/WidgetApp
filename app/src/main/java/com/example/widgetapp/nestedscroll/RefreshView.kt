package com.example.widgetapp.nestedscroll

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Scroller
import androidx.core.view.NestedScrollingParent
import androidx.core.view.NestedScrollingParent3
import androidx.core.view.NestedScrollingParentHelper
import androidx.core.view.children
import kotlin.math.abs
import kotlin.math.min

class RefreshView(context: Context,attrs:AttributeSet?) : ViewGroup(context, attrs), NestedScrollingParent3 {
    constructor(context: Context):this(context,null)
    private val TAG = "RefreshView"
    private val mHelper = NestedScrollingParentHelper(this)
    private var mOffset = 0
    private val mHeaderHeight : Int
        get() = getChildAt(0).measuredHeight
    private val mHeaderWidth : Int
        get() = getChildAt(0).measuredWidth
    private val mScroller = Scroller(context)
    private var mIsRefreshing = false
    private var mProgress = 0.0f
    private val mHeaderView:View
        get() = getChildAt(0)
    private val mContentView:View
        get() = getChildAt(1)

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val header = getChildAt(0)
        val content = getChildAt(1)
        var top = -header.measuredHeight/2
        header.layout(l,top,l+header.measuredWidth,top + header.measuredHeight)
        top = 0
        content.layout(l,top,l+content.measuredWidth,content.measuredHeight)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        ensureHeader()
        ensureContent()
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        for (child in children){
            measureChild(child,widthMeasureSpec,heightMeasureSpec)
        }
    }

    /**
     * Add a default header view.
     */
    private fun ensureHeader(){
        if (childCount < 2){
            val header = DefaultRefreshHeader(context)
            val param = LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT)
            addView(header,0,param)
        }
    }

    /**
     * Ensure there's two view in this view group
     */
    private fun ensureContent(){
        if (childCount > 2){
            throw IllegalArgumentException("Two child view at most.")
        }
    }

    override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {
        mScroller.forceFinished(true)
        return true
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int, type: Int) {
        mHelper.onNestedScrollAccepted(child, target, axes)
    }

    override fun onStopNestedScroll(target: View, type: Int) {
        if (abs(mOffset) < mHeaderHeight * 2 / 3){
            mScroller.forceFinished(true)
            mScroller.startScroll(0, abs(mOffset),0,-abs(mOffset))
            invalidate()
        }else{
            setRefreshing(true)
        }
        mHelper.onStopNestedScroll(target)
    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        var shouldScroll = 0
        if (dyUnconsumed < 0) {
            shouldScroll = -min(abs(dyUnconsumed), abs(mHeaderHeight - abs(mOffset)))
            if (abs(mOffset) >= mHeaderHeight) {
                shouldScroll = reduce(shouldScroll)
            }
        }
        shouldScroll = reduce(shouldScroll)
        mOffset += shouldScroll
        consumed[1] = shouldScroll
        contentTranslationBy(0,shouldScroll)
        updateProgress()
    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int
    ) {
        var shouldScroll = 0
        if (dyUnconsumed < 0) {
            shouldScroll = -min(abs(dyUnconsumed), abs(mHeaderHeight - abs(mOffset)))
            if (abs(mOffset) >= mHeaderHeight) {
                shouldScroll = reduce(shouldScroll)
            }
        }
        shouldScroll = reduce(shouldScroll)
        mOffset += shouldScroll
        contentTranslationBy(0,shouldScroll)
        updateProgress()
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        if (mIsRefreshing){
            return
        }
        var shouldScroll = 0
        if (dy > 0){
            if (abs(mOffset) >= 0){
                shouldScroll = min(abs(mOffset),dy)
            }
        }
        if (shouldScroll > 0){
            shouldScroll = reduce(shouldScroll)
            mOffset += shouldScroll
            consumed[1] = shouldScroll
            contentTranslationBy(0,shouldScroll)
            updateProgress()
        }
    }

    override fun onNestedFling(
        target: View,
        velocityX: Float,
        velocityY: Float,
        consumed: Boolean
    ): Boolean {
        if (mIsRefreshing){
            return false
        }
        if (velocityY < 0){
            Log.e(TAG,"Fling, v:${velocityY}")
            mScroller.fling(0, abs(mOffset),0,-velocityY.toInt(),0,0,0,mHeaderHeight)
            invalidate()
            return true
        }
        return false
    }

    override fun onNestedPreFling(target: View, velocityX: Float, velocityY: Float): Boolean {
        if (mIsRefreshing){
            return false
        }
        if (abs(mOffset) > 0){
            mScroller.fling(0,abs(mOffset),0,-velocityY.toInt(),0,0,0,mHeaderHeight)
            invalidate()
            return true
        }
        return false
    }

    override fun computeScroll() {
        if (mScroller.computeScrollOffset()){
            val shouldScroll = mScroller.currY
            Log.e(TAG,"currY:${mScroller.currY}")
            contentTranslationBy(0, abs(mOffset) - shouldScroll)
            mOffset = -shouldScroll
            invalidate()
            updateProgress()
        }
    }

    private fun reduce(origin:Int) : Int{
        return (0.5f * origin).toInt()
    }

    private fun contentTranslationBy(x:Int,y:Int){
        Log.e(TAG,"Content translation:${y}")
        mContentView.top += -y
    }

    override fun getNestedScrollAxes(): Int {
        return mHelper.nestedScrollAxes
    }

    private fun updateProgress(){
        mProgress = abs(mOffset) / mHeaderHeight.toFloat()
        val translationY = mProgress * mHeaderHeight / 2
        mHeaderView.translationY = translationY
    }

    fun setRefreshing(refresh:Boolean){
        if (mIsRefreshing == refresh){
            return
        }
        mIsRefreshing = refresh
        if (refresh){
            mScroller.startScroll(0, abs(mOffset),0,abs(mHeaderHeight - abs(mOffset)))
        }else{
            mScroller.startScroll(0, abs(mOffset),0, -abs(mOffset))
        }
        invalidate()
    }
    fun isRefreshing():Boolean{
        return mIsRefreshing
    }
}