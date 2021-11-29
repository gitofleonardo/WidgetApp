package com.example.widgetapp

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

abstract class BaseActivity<T:ViewBinding>: AppCompatActivity() {
    protected val binding by lazy { instance() }

    private fun instance():T{
        val clazz = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<*>
        val method = clazz.getMethod("inflate",LayoutInflater::class.java)
        return method.invoke(null,layoutInflater) as T
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        onInit(savedInstanceState)
    }

    protected abstract fun onInit(savedInstanceState: Bundle?)
}