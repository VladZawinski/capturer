package com.vladimir.capturer.api

import android.content.Context
import android.util.Log
import com.vladimir.capturer.internal.data.repository.RepositoryProvider
import com.vladimir.capturer.internal.support.Logger

public object Capturer {
    fun withContext(context: Context) {
        RepositoryProvider.initialize(context)
    }
    fun getHistoryAsTxt() {}

    internal var logger: Logger =
        object : Logger {
            val TAG = "Capturer"

            override fun info(
                message: String,
                throwable: Throwable?,
            ) {
                Log.i(TAG, message, throwable)
            }

            override fun warn(
                message: String,
                throwable: Throwable?,
            ) {
                Log.w(TAG, message, throwable)
            }

            override fun error(
                message: String,
                throwable: Throwable?,
            ) {
                Log.e(TAG, message, throwable)
            }
        }
}
