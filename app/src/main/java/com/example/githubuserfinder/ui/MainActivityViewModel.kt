package com.example.githubuserfinder.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuserfinder.data.BaseResponse
import com.example.githubuserfinder.data.repository.SearchRepository
import com.example.githubuserfinder.model.User
import com.example.githubuserfinder.utils.Resource

class MainActivityViewModel(private val repository: SearchRepository) : ViewModel() {
    private val userLiveData = MutableLiveData<Resource<BaseResponse<List<User>>>>()

    suspend fun getUserList(name: String, page: Int): LiveData<Resource<BaseResponse<List<User>>>> {
        userLiveData.postValue(repository.getUsers(name, page))
        return userLiveData
    }
}