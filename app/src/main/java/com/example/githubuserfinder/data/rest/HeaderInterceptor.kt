package com.example.githubuserfinder.data.rest

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor: Interceptor {
    companion object{
        const val ACCEPT_HEADER_VALUE = "application/vnd.github.v3+json"
        const val ACCEPT_HEADER_NAME = "Accept"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader(
                ACCEPT_HEADER_NAME,
                ACCEPT_HEADER_VALUE
            )
            .build()

        return chain.proceed(request)
    }
}