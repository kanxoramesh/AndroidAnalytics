package com.analytics.analytics_android.core

import com.analytics.analytics_android.AnalyticsEvent
import java.util.UUID
import java.time.Instant

class AnalyticsSession{
    val sessionId: String = UUID.randomUUID().toString()
    val startTime: Instant = Instant.now()
    var endTime: Instant? = null
    private val events: MutableList<AnalyticsEvent> = mutableListOf()

    fun addEvent(event: AnalyticsEvent) {
        events.add(event)
    }

    fun endSession() {
        endTime = Instant.now()
    }

    fun getSessionData(): Map<String, Any?> {
        return mapOf(
            "sessionId" to sessionId,
            "startTime" to startTime,
            "endTime" to endTime,
            "events" to events.map { it.getEventData() }
        )
    }
}
