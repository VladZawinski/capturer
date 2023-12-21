package com.vladimir.capturer.internal.support

import android.content.Context
import com.vladimir.capturer.R
import com.vladimir.capturer.internal.data.entity.HttpTransaction
import okio.Buffer
import okio.Source

internal class TransactionListDetailsSharable(
    transactions: List<HttpTransaction>,
    encodeUrls: Boolean,
) : Sharable {
    private val transactions = transactions.map { TransactionDetailsSharable(it, encodeUrls) }

    override fun toSharableContent(context: Context): Source =
        Buffer().writeUtf8(
            transactions.joinToString(
                separator = "\n${context.getString(R.string.chucker_export_separator)}\n",
                prefix = "${context.getString(R.string.chucker_export_prefix)}\n",
                postfix = "\n${context.getString(R.string.chucker_export_postfix)}\n",
            ) { it.toSharableUtf8Content(context) },
        )
}