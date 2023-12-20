package com.vladimir.capturer.internal.data.repository

import com.vladimir.capturer.internal.data.entity.HttpTransaction
import com.vladimir.capturer.internal.data.room.CapturerDatabase

internal class HttpTransactionRepositoryImpl(
    private val database: CapturerDatabase,
): HttpTransactionRepository {
    override suspend fun insertTransaction(transaction: HttpTransaction) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllTransactions() {
        TODO("Not yet implemented")
    }

    override suspend fun getAllTransactions(): List<HttpTransaction> {
        TODO("Not yet implemented")
    }

}