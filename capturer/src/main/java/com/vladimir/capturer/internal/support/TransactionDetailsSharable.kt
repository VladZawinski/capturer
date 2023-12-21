package com.vladimir.capturer.internal.support

import android.content.Context
import com.vladimir.capturer.internal.data.entity.HttpTransaction
import okio.Buffer
import okio.Source

internal class TransactionDetailsSharable(
    private val transaction: HttpTransaction,
    private val encodeUrls: Boolean,
) : Sharable {
    override fun toSharableContent(context: Context): Source =
        Buffer().apply {
            writeUtf8("Url: ${transaction.url}\n")
            writeUtf8("Request Body: ${transaction.getFormattedRequestBody()}\n")
            writeUtf8("Response Body: ${transaction.getFormattedResponseBody()}\n")
        }
}