package com.vladimir.capturer.internal.support

import com.vladimir.capturer.api.Capturer

internal interface Logger {
    fun info(
        message: String,
        throwable: Throwable? = null,
    )

    fun warn(
        message: String,
        throwable: Throwable? = null,
    )

    fun error(
        message: String,
        throwable: Throwable? = null,
    )

    companion object : Logger {
        override fun info(
            message: String,
            throwable: Throwable?,
        ) {
            Capturer.logger.info(message, throwable)
        }

        override fun warn(
            message: String,
            throwable: Throwable?,
        ) {
            Capturer.logger.warn(message, throwable)
        }

        override fun error(
            message: String,
            throwable: Throwable?,
        ) {
            Capturer.logger.error(message, throwable)
        }
    }
}