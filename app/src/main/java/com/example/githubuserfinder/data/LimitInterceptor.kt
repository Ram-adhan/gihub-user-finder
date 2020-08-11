package com.example.githubuserfinder.data

import okhttp3.Interceptor
import okhttp3.Response

class LimitInterceptor: Interceptor {
    companion object{
        const val LIMIT_REMAINING = "X-Ratelimit-Remaining"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        return response
    }
}