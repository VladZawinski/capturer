package com.vladimir.capturer.internal.data.entity

import android.content.Context
import com.vladimir.capturer.internal.support.FormatUtils
import com.vladimir.capturer.internal.support.FormattedUrl
import com.vladimir.capturer.internal.support.SpanTextUtil
import okhttp3.HttpUrl
import java.util.Date

internal class HttpTransaction(
    var method: String?,
    var host: String?,
    var path: String?,
    var url: String?,
    var requestDate: Long?,
    var responseDate: Long?,
    var requestPayloadSize: Long?,
    var requestHeaders: String?,
    var requestContentType: String?,
    var requestBody: String?,
    var responseCode: Int?,
    var responseMessage: String?,
    var responseContentType: String?,
    var responseBody: String?,
    var scheme: String?,
) {
    val requestDateString: String?
        get() = requestDate?.let { Date(it).toString() }
    val responseDateString: String?
        get() = responseDate?.let { Date(it).toString() }
    val isSsl: Boolean
        get() = scheme.equals("https", ignoreCase = true)
    private fun formatBody(
        body: String,
        contentType: String?,
    ): String {
        return when {
            contentType.isNullOrBlank() -> body
            contentType.contains("json", ignoreCase = true) -> FormatUtils.formatJson(body)
            contentType.contains("xml", ignoreCase = true) -> FormatUtils.formatXml(body)
            contentType.contains("x-www-form-urlencoded", ignoreCase = true) ->
                FormatUtils.formatUrlEncodedForm(body)
            else -> body
        }
    }
    private fun spanBody(
        body: CharSequence,
        contentType: String?,
        context: Context?,
    ): CharSequence {
        return when {
            // TODO Implement Other Content Types
            contentType.isNullOrBlank() -> body
            contentType.contains("json", ignoreCase = true) && context != null -> {
                SpanTextUtil(context).spanJson(body)
            }
            else -> formatBody(body.toString(), contentType)
        }
    }
    fun getFormattedRequestBody(): String {
        return requestBody?.let { formatBody(it, requestContentType) } ?: ""
    }
    fun populateUrl(httpUrl: HttpUrl): HttpTransaction {
        val formattedUrl = FormattedUrl.fromHttpUrl(httpUrl, encoded = false)
        url = formattedUrl.url
        host = formattedUrl.host
        path = formattedUrl.pathWithQuery
        scheme = formattedUrl.scheme
        return this
    }
}


