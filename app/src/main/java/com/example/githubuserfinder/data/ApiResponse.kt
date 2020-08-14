package com.example.githubuserfinder.data

import com.example.githubuserfinder.utils.Resource
import retrofit2.Response

abstract class ApiResponse {

    protected suspend fun <T> getResult(call: suspend () -> Response<T>): Resource<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) return Resource.success(body)
            }
            return error("${response.code()} ${response.message()}", response.code())
        } catch (e: Exception) {
            return error(e.message ?: e.toString())
        }
    }

    private fun <T> error(message: String, code: Int = 999): Resource<T> {
        return Resource.error("Network call failed, reason: $message", code = code)
    }
}