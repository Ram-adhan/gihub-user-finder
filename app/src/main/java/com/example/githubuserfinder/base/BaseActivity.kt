package com.example.githubuserfinder.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResource())
        setupLibrary()
        setupIntent()
        setupProcess()
        setupAction()
    }

    protected abstract fun getLayoutResource(): Int

    protected open fun setupLibrary() {}

    protected open fun setupIntent() {}

    protected open fun setupProcess() {}

    protected abstract fun setupAction()

}