package com.example.githubuserfinder.di

import com.example.githubuserfinder.ui.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    NetworkModule::class
])
interface AppComponent {
    fun inject(activity: MainActivity)
}