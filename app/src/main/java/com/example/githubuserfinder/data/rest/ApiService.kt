package com.example.githubuserfinder.data.rest

import com.example.githubuserfinder.data.BaseResponse
import com.example.githubuserfinder.model.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("/search/users?")
    suspend fun getUsers(@Query("q") q: String?, @Query("page") page:Int): Response<BaseResponse<List<User>>>
}