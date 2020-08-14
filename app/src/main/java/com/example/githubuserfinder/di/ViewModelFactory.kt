package com.example.githubuserfinder.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubuserfinder.data.repository.SearchRepository
import com.example.githubuserfinder.ui.MainActivityViewModel
import java.lang.IllegalArgumentException
import javax.inject.Inject

class ViewModelFactory @Inject constructor(private val searchRepository: SearchRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainActivityViewModel::class.java) -> MainActivityViewModel(
                searchRepository
            ) as T

            else -> throw IllegalArgumentException("Unknown ViewModel Class ${modelClass::class.java.simpleName}")
        }
    }
}