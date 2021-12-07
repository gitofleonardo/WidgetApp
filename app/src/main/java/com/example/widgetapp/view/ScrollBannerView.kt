package com.example.widgetapp.view

import android.animation.LayoutTransition
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.content.res.Resources
import android.os.Looper
import android.os.Message
import android.text.SpannableString
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.widgetapp.R
import com.example.widgetapp.view.ScrollBannerView.Companion.scaledPx
import java.lang.ref.WeakReference
import java.util.*

class ScrollBannerView(context: Context,attrs:AttributeSet?,defStyleAttr:Int) :FrameLayout(context, attrs, defStyleAttr), Callback {
    constructor(context: Context,attrs: AttributeSet?):this(context,attrs,-1)
    constructor(context: Context):this(context,null)

    private var mCurrentTextView:TextView? = null
    private var mCurrentDisplayTextIndex = 0
    private var mTextList = mutableListOf<String>()
    private var mScrollTimer:Timer? = null
    private var mScrollPeriod:Long = 2000L
    private var mScrollDuration:Long = 500L
    private var mHandler = Handler(this)
    private val mPlaceHolderTextView by lazy { generateTextView(" ") }
    private val mHeight:Int
        get() {
            mPlaceHolderTextView.measure(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
            return mPlaceHolderTextView.measuredHeight
        }
    private var mTextSize:Float = 0.0f

    init {
        val arr = context.obtainStyledAttributes(attrs,R.styleable.ScrollBannerView)
        val sp = arr.getDimensionPixelSize(R.styleable.ScrollBannerView_textSize,16.scalePx(context))
        mTextSize = sp.toFloat()
        arr.recycle()
        addView(mPlaceHolderTextView)
    }

    private fun generateTextView(str:String):TextView{
        val tv = TextView(context)
        val param = LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT)
        tv.apply {
            maxLines = 1
            layoutParams = param
            text = SpannableString(str)
            textSize = mTextSize
            gravity = Gravity.CENTER
        }
        return tv
    }

    fun startScroll(texts:List<String>,duration:Long,period:Long){
        mTextList.clear()
        if (texts.isEmpty()){
            return
        }
        mTextList.addAll(texts)
        mCurrentDisplayTextIndex = 0
        mScrollDuration = duration
        mScrollPeriod = period

        val showPropEnter = PropertyValuesHolder.ofFloat("alpha",0.0f,1.0f)
        val translatePropEnter = PropertyValuesHolder.ofFloat("translationY",-mHeight.toFloat(),0.0f)
        val enterAnimator = ObjectAnimator.ofPropertyValuesHolder(this,showPropEnter,translatePropEnter)

        layoutTransition = LayoutTransition().apply {
            setAnimator(LayoutTransition.APPEARING,enterAnimator)
            setDuration(LayoutTransition.APPEARING,mScrollDuration)
        }
        scrollText()
    }

    fun stopScroll(){
        mScrollTimer?.cancel()
    }

    private fun scrollText(){
        mScrollTimer?.cancel()
        mScrollTimer = Timer().apply {
            schedule(Task(),0L,mScrollPeriod)
        }
    }

    private inner class Task:TimerTask(){
        override fun run() {
            mHandler.sendEmptyMessage(0)
        }
    }

    private class Handler(callback: com.example.widgetapp.view.Callback) : android.os.Handler(Looper.getMainLooper()){
        private val mWeak = WeakReference(callback)
        override fun handleMessage(msg: Message) {
            mWeak.get()?.onHandleScroll()
        }
    }

    companion object{
        fun Float.scaledPx(context: Context):Float{
            val scale = context.resources.displayMetrics.scaledDensity
            return this * scale + 0.5f
        }
        fun Int.scalePx(context: Context):Int{
            val scale = context.resources.displayMetrics.scaledDensity
            return (this * scale + 0.5f).toInt()
        }
    }

    private fun animateRemoveView(view: View){
        val dimPropExit = PropertyValuesHolder.ofFloat("alpha",1.0f,0.0f)
        val translatePropExit = PropertyValuesHolder.ofFloat("translationY",0.0f,mHeight.toFloat())
        val exitAnimator = ObjectAnimator.ofPropertyValuesHolder(view,dimPropExit,translatePropExit)
        exitAnimator.start()
    }

    override fun onHandleScroll() {
        val curIndex = mCurrentDisplayTextIndex++
        mCurrentDisplayTextIndex %= mTextList.size

        val toRemoveView = mCurrentTextView
        val tv = generateTextView(mTextList[curIndex])
        addView(tv)
        mCurrentTextView = tv
        toRemoveView?.let {
            animateRemoveView(it)
        }
    }
}

private interface Callback{
    fun onHandleScroll()
}
