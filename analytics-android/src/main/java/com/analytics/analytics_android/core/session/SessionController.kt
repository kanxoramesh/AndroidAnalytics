package com.analytics.analytics_android.core.session

import com.analytics.analytics_android.AnalyticsEvent
import com.analytics.analytics_android.Logger
import com.analytics.analytics_android.Session
import com.analytics.analytics_android.Storage
import com.analytics.analytics_android.core.storage.AnalyticsEventEntity
import com.analytics.analytics_android.core.storage.AnalyticsSessionEntity
import com.google.gson.Gson
import java.time.Instant

class SessionController(private val logger: Logger?, private val storage: Storage) {
    private var currentSession: AnalyticsSession = AnalyticsSession()
    private val gson = Gson()

    init {
        logger?.info("Session started: ${currentSession?.sessionId}")

    }

    fun startSession() {
        storage.saveSession(AnalyticsSessionEntity(currentSession.sessionId, currentSession.startTime))
    }

    fun endSession() {
        storage.updateSession(currentSession.sessionId, Instant.now().toEpochMilli() )
    }

    fun getSessions(): List<Session>  {
        return storage.getAllSessionsWithEvents().map { sessionWithEvents ->
            val session = sessionWithEvents.session
            val events = sessionWithEvents.events.map { eventEntity ->
                gson.fromJson(eventEntity.toJson(), AnalyticsEvent::class.java)
            }
            var resSession=AnalyticsSession(session.sessionId,session.startTime,session.endTime)
            resSession.events.addAll(events)
            resSession
        }
    }

    fun addEvent(eventName: String, properties: Map<String, Any>) {
        storage.saveEvent(
            AnalyticsEventEntity(
                eventName = eventName,
                sessionId = currentSession.sessionId,
                properties = gson.toJson(properties)
            )
        )
        logger?.info("Event added: $eventName with properties: $properties")

    }


}