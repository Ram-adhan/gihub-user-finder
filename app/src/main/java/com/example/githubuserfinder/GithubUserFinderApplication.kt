package com.example.githubuserfinder

import android.app.Application
import com.example.githubuserfinder.di.AppComponent
import com.example.githubuserfinder.di.DaggerAppComponent

class GithubUserFinderApplication : Application() {
    lateinit var component: AppComponent
    override fun onCreate() {
        super.onCreate()

        component = DaggerAppComponent.builder().build()
    }
}