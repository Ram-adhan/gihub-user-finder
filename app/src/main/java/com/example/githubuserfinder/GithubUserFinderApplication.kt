package com.example.githubuserfinder

import android.app.Application
import android.content.Context
import com.example.githubuserfinder.di.AppComponent
import com.example.githubuserfinder.di.DaggerAppComponent

class GithubUserFinderApplication : Application() {
    lateinit var component: AppComponent

    companion object{
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        component = DaggerAppComponent.builder().build()
    }
}