package com.example.githubuserfinder.data.repository

import com.example.githubuserfinder.data.ApiResponse
import com.example.githubuserfinder.data.rest.ApiService
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val service: ApiService
) : ApiResponse() {

    suspend fun getUsers(name: String, page: Int) = getResult { service.getUsers(name, page) }
}