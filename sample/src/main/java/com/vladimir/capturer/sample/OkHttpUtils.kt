package com.vladimir.capturer.sample

import android.content.Context
import com.vladimir.capturer.api.CapturerInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

fun createOkHttpClient(
    context: Context,
): OkHttpClient {
    return OkHttpClient.Builder()
        // Add a ChuckerInterceptor instance to your OkHttp client as an application or a network interceptor.
        // Learn more about interceptor types here – https://square.github.io/okhttp/interceptors.
        // "activeForType" is needed only in this sample to control it from the UI.
        .addInterceptor(CapturerInterceptor(context))
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()
}