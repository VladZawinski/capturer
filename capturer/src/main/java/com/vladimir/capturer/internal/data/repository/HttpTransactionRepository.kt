package com.vladimir.capturer.internal.data.repository

import com.vladimir.capturer.internal.data.entity.HttpTransaction

internal interface HttpTransactionRepository {
    suspend fun insertTransaction(transaction: HttpTransaction)
    suspend fun deleteAllTransactions()
    suspend fun updateTransaction(transaction: HttpTransaction): Int
    suspend fun getAllTransactions(): List<HttpTransaction>
}
