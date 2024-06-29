package com.analytics.analytics_android

import android.net.Uri
import com.analytics.analytics_android.network.HttpMethod
import com.analytics.analytics_android.network.Request
import com.analytics.analytics_android.network.RequestResult

interface NetworkSynchronizer {
    /**
     * Send request to the collector.
     * @param request to send.
     * @return result of the sending operation.
     */
    fun sendRequest(request: Request): RequestResult

    /**
     * GET or POST used to send request to the collector.
     * @return http method used
     */
    val httpMethod: HttpMethod

    /**
     * Event collector URI.
     * @return URI of the collector.
     */
    val uri: Uri
}