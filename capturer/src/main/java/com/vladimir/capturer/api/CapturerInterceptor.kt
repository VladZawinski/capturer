package com.vladimir.capturer.api

import android.content.Context
import com.vladimir.capturer.internal.support.CacheDirectoryProvider
import com.vladimir.capturer.internal.support.PlainTextDecoder
import com.vladimir.capturer.internal.support.RequestProcessor
import com.vladimir.capturer.internal.support.ResponseProcessor
import okhttp3.Interceptor
import okhttp3.Response

class CapturerInterceptor(
    private val context: Context,
) : Interceptor {
    private val decoders = emptyList<BodyDecoder>() + BUILT_IN_DECODERS
    private val requestProcessor =
        RequestProcessor(
            MAX_CONTENT_LENGTH,
            decoders,
        )
    private val responseProcessor =
        ResponseProcessor(
            MAX_CONTENT_LENGTH,
            decoders,
        )
    override fun intercept(chain: Interceptor.Chain): Response {
        TODO("Not yet implemented")
    }
    private companion object {
        private const val MAX_CONTENT_LENGTH = 250_000L

        private val BUILT_IN_DECODERS = listOf(PlainTextDecoder)
    }
}
