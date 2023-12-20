package com.vladimir.capturer.api

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

class CapturerInterceptor(
    private val context: Context,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        TODO("Not yet implemented")
    }
}
