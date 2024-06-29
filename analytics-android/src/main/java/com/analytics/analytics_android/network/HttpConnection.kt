package com.analytics.analytics_android.network

import android.net.TrafficStats
import android.net.Uri
import androidx.annotation.RestrictTo
import com.analytics.analytics_android.NetworkSynchronizer
import com.analytics.analytics_android.core.logger.AnalyticsLogger
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.util.concurrent.Callable
import java.util.concurrent.TimeUnit

/**
 * Components in charge to send events to the collector.
 * It uses OkHttp as Http client.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
class HttpConnection(builder: AndroidAnalytics.OkHttpNetworkConnectionBuilder) :
    NetworkSynchronizer {

    private val networkUri: String
    override val httpMethod: HttpMethod
    private val timeout: Int

    private val requestHeaders: Map<String, String>?
    private var client: OkHttpClient? = null
    private val uriBuilder: Uri.Builder
    override val uri: Uri
        get() = uriBuilder.clearQuery().build()




    init {
        // Decode uri to extract protocol
        var tempUri = builder.uri
        val url = Uri.parse(builder.uri)
        if (url.scheme == null) {
            tempUri = "https://" + builder.uri
        } else {
            when (url.scheme) {
                "https" -> {}
                "http" -> {}
                else -> tempUri = "https://" + builder.uri
            }
        }

        // Configure
        networkUri = tempUri
        httpMethod = builder.httpMethod
        timeout = builder.timeout
        requestHeaders = builder.requestHeaders
        uriBuilder = Uri.parse(networkUri).buildUpon()

        // Configure with external OkHttpClient
        client = if (builder.client == null) {
            OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build()
        } else {
            builder.client
        }
    }

    override fun sendRequest(request: Request): RequestResult {
        val okHttpRequest = buildPostRequest(request)
        var future = Executor.futureCallable(getRequestCallable(okHttpRequest))
        val tempCode = future[timeout.toLong(), TimeUnit.SECONDS] as? Int
       return RequestResult(tempCode?:-1,request.payload.map { it.sessionId!! })
    }


    /**
     * Builds an OkHttp POST request which is ready
     * to be executed.
     * @param request The request where to get the payload to be sent.
     * @return An OkHttp request object.
     */
    private fun buildPostRequest(request: Request): okhttp3.Request {
        val reqUrl = uriBuilder.build().toString()
        val reqBody = request.payload.toString().toRequestBody()

        val builder = okhttp3.Request.Builder()
            .url(reqUrl)
            .post(reqBody)
        requestHeaders?.let {
            it.forEach { (key, value) ->
                builder.header(key, value)
            }
        }
        return builder.build()
    }

    /**
     * Returns a Callable Request Send
     *
     * @param request the request to be sent
     * @return the new Callable object
     */
    private fun getRequestCallable(request: okhttp3.Request): Callable<Int> {
        return Callable { requestSender(request) }
    }

    /**
     * The function responsible for actually sending
     * the request to the collector.
     *
     * @param request The request to be sent
     * @return a RequestResult
     */
    private fun requestSender(request: okhttp3.Request): Int {
        try {
            AnalyticsLogger.info("Sending request: %s", request)
            TrafficStats.setThreadStatsTag(TRAFFIC_STATS_TAG)
            val resp = client?.newCall(request)?.execute()
            resp?.let {
                resp.body?.close()
                return resp.code
            }
            return -1
        } catch (e: IOException) {
            println(e)
            AnalyticsLogger.error(e, "Request sending failed: %s", e.toString())
            return -1
        }
    }

    companion object {
        private const val TRAFFIC_STATS_TAG = 1

    }
}
