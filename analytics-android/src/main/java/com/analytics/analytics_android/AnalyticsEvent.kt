package com.analytics.analytics_android

import java.time.Instant

class AnalyticsEvent(
    private val eventName: String,
    private val properties: Map<String, Any>,
   private val timestamp: Long = Instant.now().toEpochMilli()

) {

    fun getEventData(): Map<String, Any> {
        return mapOf(
            "eventName" to eventName,
            "properties" to properties,
            "timestamp" to timestamp
        )
    }
}
