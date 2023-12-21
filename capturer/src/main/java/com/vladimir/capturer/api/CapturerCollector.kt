package com.vladimir.capturer.api

import android.content.Context
import android.net.Uri
import com.vladimir.capturer.internal.data.entity.HttpTransaction
import com.vladimir.capturer.internal.data.repository.RepositoryProvider
import com.vladimir.capturer.internal.support.TransactionListDetailsSharable
import com.vladimir.capturer.internal.support.writeToFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

public class CapturerCollector @JvmOverloads constructor(
    context: Context,
) {
    private val scope = MainScope()
    init {
        RepositoryProvider.initialize(context)
    }
    /**
     * Call this method when you send an HTTP request.
     * @param transaction The HTTP transaction sent
     */
    internal fun onRequestSent(transaction: HttpTransaction) {
        scope.launch {
            withContext(Dispatchers.IO) {
                RepositoryProvider.transaction().insertTransaction(transaction)
            }
        }
    }
    /**
     * Call this method when you received the response of an HTTP request.
     * It must be called after [ChuckerCollector.onRequestSent].
     * @param transaction The sent HTTP transaction completed with the response
     */
    internal fun onResponseReceived(transaction: HttpTransaction) {
        scope.launch {
            val updated =
                withContext(Dispatchers.IO) {
                    RepositoryProvider.transaction().updateTransaction(transaction)
                }
        }
    }

    public suspend fun writeTransactions(
        context: Context,
        startTimestamp: Long?,
    ): Uri? {
        val transactions =
            RepositoryProvider.transaction().getAllTransactions()
        if (transactions.isEmpty()) {
            return null
        }

        val sharableTransactions = TransactionListDetailsSharable(transactions, encodeUrls = false)
        return sharableTransactions.writeToFile(
            context = context,
            fileName = "api_transactions.txt",
        )
    }

}