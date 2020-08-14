package com.example.githubuserfinder.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResource())
        setupLibrary()
        setupIntent()
        setupUI()
        setupAction()
        setupProcess()
    }

    protected abstract fun getLayoutResource(): Int

    protected open fun setupLibrary() {}

    protected open fun setupIntent() {}

    protected open fun setupUI() {}

    protected open fun setupProcess() {}

    protected abstract fun setupAction()

}