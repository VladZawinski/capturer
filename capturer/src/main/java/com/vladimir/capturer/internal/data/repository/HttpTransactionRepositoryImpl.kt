package com.vladimir.capturer.internal.data.repository

import com.vladimir.capturer.internal.data.entity.HttpTransaction
import com.vladimir.capturer.internal.data.room.CapturerDatabase

internal class HttpTransactionRepositoryImpl(
    private val database: CapturerDatabase,
): HttpTransactionRepository {
    private val transactionDao get() = database.transactionDao()
    override suspend fun insertTransaction(transaction: HttpTransaction) {
        val id = transactionDao.insert(transaction)
        transaction.id = id ?: 0
    }

    override suspend fun deleteAllTransactions() {
        transactionDao.deleteAll()
    }

    override suspend fun updateTransaction(transaction: HttpTransaction): Int {
        return transactionDao.update(transaction)
    }

    override suspend fun getAllTransactions(): List<HttpTransaction> = transactionDao.getAll()
}
