package com.analytics.analytics_android.network


/**
 * Stores the result of a Request attempt.
 * @param statusCode HTTP status code
 * @param sessionIds a list of session ids
 */
class RequestResult(
    val statusCode: Int,
    val sessionIds: List<String>
) {

    val isSuccessful: Boolean
        get() = statusCode in 200..299

    /**
     * Checks if the request should be retried.
     */
    fun shouldRetry(retryAllowed: Boolean): Boolean {
        if (isSuccessful) {
            return false
        }

        // don't retry if retries are not allowed
        if (!retryAllowed) {
            return false
        }
        //TODO implementation
        return false
    }
}
