package com.analytics.analytics_android.network

import android.net.Uri
import com.analytics.analytics_android.Session
import com.analytics.analytics_android.core.storage.SessionWithEvents
import java.util.concurrent.Callable

interface NetworkSynchronizer {
    /**
     * Send requests to the collector.
     * @param requests to send.
     * @return results of the sending operation.
     */
    fun sendRequest(requests: Request): RequestResult

    /**
     * GET or POST used to send requests to the collector.
     * @return http method used
     */
    val httpMethod: HttpMethod

    /**
     * Event collector URI.
     * @return URI of the collector.
     */
    val uri: Uri
}