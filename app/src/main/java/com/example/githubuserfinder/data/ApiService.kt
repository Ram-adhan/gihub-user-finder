package com.example.githubuserfinder.data

import android.telecom.Call
import com.example.githubuserfinder.base.BaseResponse
import com.example.githubuserfinder.model.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("/search/users?")
    fun getUsers(@Query("q") q: String?): Response<BaseResponse<List<User>>>
}