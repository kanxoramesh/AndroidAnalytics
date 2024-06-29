package com.analytics.analytics_android.core.session
import androidx.annotation.RestrictTo
import com.analytics.analytics_android.AnalyticsEvent
import com.analytics.analytics_android.Session
import java.time.Instant
import java.util.UUID

@RestrictTo(RestrictTo.Scope.LIBRARY)
class AnalyticsSession(
    override val sessionId: String = UUID.randomUUID().toString(),
    override val startTime: Long = Instant.now().toEpochMilli(),
    override var endTime: Long? = null // Initially null
) : Session {
    override val events: MutableList<AnalyticsEvent> = mutableListOf()

    override fun addEvent(event: AnalyticsEvent) {
        events.add(event)
    }

    override fun endSession() {
        endTime = Instant.now().toEpochMilli()
    }

    override fun getSessionData(): Map<String, Any?> {
        return mapOf(
            "sessionId" to sessionId,
            "startTime" to startTime,
            "endTime" to endTime,
            "events" to events.map { it.getEventData() }
        )
    }
}
