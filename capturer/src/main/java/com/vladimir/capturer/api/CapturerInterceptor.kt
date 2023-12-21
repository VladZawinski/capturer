package com.vladimir.capturer.api

import android.content.Context
import com.vladimir.capturer.internal.data.entity.HttpTransaction
import com.vladimir.capturer.internal.support.CacheDirectoryProvider
import com.vladimir.capturer.internal.support.PlainTextDecoder
import com.vladimir.capturer.internal.support.RequestProcessor
import com.vladimir.capturer.internal.support.ResponseProcessor
import okhttp3.Interceptor
import okhttp3.Response
import okio.IOException

class CapturerInterceptor(
    private val context: Context,
) : Interceptor {
    private val decoders = emptyList<BodyDecoder>() + BUILT_IN_DECODERS
    private val skipPaths = mutableSetOf<String>()
    private val collector = CapturerCollector(context)
    private val requestProcessor =
        RequestProcessor(
            MAX_CONTENT_LENGTH,
            decoders,
            collector
        )
    private val responseProcessor =
        ResponseProcessor(
            MAX_CONTENT_LENGTH,
            decoders,
            CacheDirectoryProvider { context.filesDir },
            collector
        )
    override fun intercept(chain: Interceptor.Chain): Response {
        val transaction = HttpTransaction()
        val request = chain.request()
        val shouldProcessTheRequest = !skipPaths.any { it == request.url.encodedPath }
        if (shouldProcessTheRequest) {
            requestProcessor.process(request, transaction)
        }
        val response =
            try {
                chain.proceed(request)
            } catch (e: IOException) {
                transaction.error = e.toString()
                collector.onResponseReceived(transaction)
                throw e
            }
        return if (shouldProcessTheRequest) {
            responseProcessor.process(response, transaction)
        } else {
            response
        }
    }
    private companion object {
        private const val MAX_CONTENT_LENGTH = 250_000L

        private val BUILT_IN_DECODERS = listOf(PlainTextDecoder)
    }
}
