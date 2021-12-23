package com.example.widgetapp.view

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class OpenGLView(context: Context,attributeSet: AttributeSet?) : GLSurfaceView(context,attributeSet) {
    constructor(context: Context) : this(context,null)

    init {
        setRenderer(GLRenderer())
    }
}

class GLRenderer() : GLSurfaceView.Renderer{
    override fun onSurfaceCreated(gl: GL10, config: EGLConfig?) {

    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {

    }

    override fun onDrawFrame(gl: GL10) {

    }
}