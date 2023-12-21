package com.vladimir.capturer.internal.support

import com.vladimir.capturer.api.BodyDecoder
import com.vladimir.capturer.api.CapturerCollector
import com.vladimir.capturer.internal.data.entity.HttpTransaction
import okhttp3.Request
import okio.Buffer
import okio.ByteString
import okio.IOException

internal class RequestProcessor(
    private val maxContentLength: Long,
    private val bodyDecoders: List<BodyDecoder>,
    private val collector: CapturerCollector,
) {
    fun process(
        request: Request,
        transaction: HttpTransaction,
    ) {
        processMetadata(request, transaction)
        processPayload(request, transaction)
        collector.onRequestSent(transaction)
    }
    private fun processMetadata(
        request: Request,
        transaction: HttpTransaction,
    ) {
        transaction.apply {
            populateUrl(request.url)
            requestDate = System.currentTimeMillis()
            method = request.method
            requestPayloadSize = request.body?.contentLength()
        }
    }
    private fun processPayload(
        request: Request,
        transaction: HttpTransaction,
    ) {
        val body = request.body ?: return
        if (body.isOneShot()) {
            Logger.info("Skipping one shot request body")
            return
        }
        if (body.isDuplex()) {
            Logger.info("Skipping duplex request body")
            return
        }

        val requestSource =
            try {
                Buffer().apply { body.writeTo(this) }
            } catch (e: IOException) {
                Logger.error("Failed to read request payload", e)
                return
            }
        val limitingSource = LimitingSource(requestSource.uncompress(request.headers), maxContentLength)

        val contentBuffer = Buffer().apply { limitingSource.use { writeAll(it) } }

        val decodedContent = decodePayload(request, contentBuffer.readByteString())
        transaction.requestBody = decodedContent
    }

    private fun decodePayload(
        request: Request,
        body: ByteString,
    ) = bodyDecoders.asSequence()
        .mapNotNull { decoder ->
            try {
                Logger.info("Decoding with: $decoder")
                decoder.decodeRequest(request, body)
            } catch (e: IOException) {
                Logger.warn("Decoder $decoder failed to process request payload", e)
                null
            }
        }.firstOrNull()
}