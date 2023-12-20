package com.vladimir.capturer.internal.data.repository

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.vladimir.capturer.internal.data.room.CapturerDatabase

internal object RepositoryProvider {
    private var transactionRepository: HttpTransactionRepository? = null

    fun transaction(): HttpTransactionRepository {
        return checkNotNull(transactionRepository) {
            "You can't access the transaction repository if you don't initialize it!"
        }
    }

    /**
     * Idempotent method. Must be called before accessing the repositories.
     */
    fun initialize(applicationContext: Context) {
        if (transactionRepository == null) {
            val db = CapturerDatabase.create(applicationContext)
            transactionRepository = HttpTransactionRepositoryImpl(db)
        }
    }

    /**
     * Cleanup stored singleton objects
     */
    @VisibleForTesting
    fun close() {
        transactionRepository = null
    }
}