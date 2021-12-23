package com.example.widgetapp.view

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toDrawable

class BlurImageView(context: Context,attrs:AttributeSet?) : AppCompatImageView(context,attrs) {
    constructor(context: Context) : this(context,null)
    var blurRadius:Int = 5
        set(value) {
            field = value
            postInvalidate()
        }
    private val mPaint by lazy {
        Paint().apply {
            maskFilter = BlurMaskFilter(10.0f,BlurMaskFilter.Blur.NORMAL)
        }
    }
    private val mMatrix = Matrix().apply {
        setScale(2.0f,2.0f)
    }

    override fun onDraw(canvas: Canvas) {
        val bitmap = drawable.toBitmap()
//        setImageDrawable(blurBitmap(bitmap))
//        super.onDraw(canvas)
        canvas.drawBitmap(blurBitmapWithRenderScript(bitmap),mMatrix,mPaint)
    }

    private fun blurBitmapWithRenderScript(bitmap: Bitmap) : Bitmap{
        val outputBitmap = Bitmap.createBitmap(bitmap)
        val rs = RenderScript.create(context)
        val blur = ScriptIntrinsicBlur.create(rs, Element.A_8(rs))
        val tmpIn = Allocation.createFromBitmap(rs,bitmap)
        val tmpOut = Allocation.createFromBitmap(rs,outputBitmap)
        blur.setRadius(5.0f)
        blur.setInput(tmpIn)
        blur.forEach(tmpOut)
        tmpOut.copyTo(outputBitmap)
        return outputBitmap
    }

    private fun blurBitmap(bitmap: Bitmap) : Drawable{
        val bWidthPixels = bitmap.width
        val bHeightPixels = bitmap.height
        val inPixels = IntArray(bWidthPixels * bHeightPixels)
        val outPixels = IntArray(bWidthPixels * bHeightPixels)
        //get origin pixels
        bitmap.getPixels(inPixels,0,bWidthPixels,0,0,bWidthPixels,bHeightPixels)
        // do blur function
        blurPixels(inPixels, outPixels, bWidthPixels)
        //create new bitmap
        val newBitmap = Bitmap.createBitmap(bWidthPixels,bHeightPixels,Bitmap.Config.ARGB_8888)
        newBitmap.setPixels(outPixels,0,bWidthPixels,0,0,bWidthPixels,bHeightPixels)
        return newBitmap.toDrawable(context.resources)
    }

    private fun blurPixels(inPixels:IntArray,outPixels:IntArray,widthStride:Int){
        val height = inPixels.size / widthStride
        for (i in 0 until widthStride) {
            for (j in 0 until height) {
                blurPixel(inPixels, outPixels, widthStride, i, j)
            }
        }
    }

    private fun blurPixel(inPixels: IntArray,outPixels: IntArray,stride: Int,x:Int,y:Int){
        val pixel = getPixel(inPixels,stride,x,y)

    }


    private fun getPixel(pixels: IntArray,stride:Int,x:Int,y:Int) : Int {
        return pixels[stride * x + y]
    }
}