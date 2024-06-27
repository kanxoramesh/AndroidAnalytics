package com.analytics.analytics_android

import java.time.Instant


/**
 * Interface for managing Sessions.
 */
interface Session {


    /**
     * The session identifier.
     * A unique identifier which is used to identify the session.
     */
    val sessionId: String?

    val startTime: Long?
    val endTime: Long?
    val events: List<AnalyticsEvent>


    fun addEvent(event: AnalyticsEvent)
    fun endSession()
    fun getSessionData(): Map<String, Any?>
}
