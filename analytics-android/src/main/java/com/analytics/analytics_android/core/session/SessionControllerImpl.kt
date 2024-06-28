package com.analytics.analytics_android.core.session

import com.analytics.analytics_android.AnalyticsEvent
import com.analytics.analytics_android.Logger
import com.analytics.analytics_android.Session
import com.analytics.analytics_android.SessionController
import com.analytics.analytics_android.Storage
import com.analytics.analytics_android.core.storage.AnalyticsEventEntity
import com.analytics.analytics_android.core.storage.AnalyticsSessionEntity
import com.analytics.analytics_android.utils.jsonStringToMap
import com.google.gson.Gson
import java.time.Instant

class SessionControllerImpl(private val logger: Logger?, private val storage: Storage) :
    SessionController {
    private val gson = Gson()
    private var currentSessionInternal: Session? = null

    override var currentSession: Session?
        get() = currentSessionInternal
        set(value) {
            currentSessionInternal = value
        }

    private fun startSessionIfNeeded() {
        if (currentSession == null) {
            currentSession = AnalyticsSession()
            currentSession?.let {
                storage.saveSession(AnalyticsSessionEntity(it.sessionId!!, it.startTime!!))
                logger?.info("Session started: ${it.sessionId}")
            }
        } else {
            logger?.info("Session already created: ${currentSession?.sessionId}")
        }

    }

    override fun startSession() {
        startSessionIfNeeded()
    }

    override fun pause() {
        TODO("Not yet implemented")
    }

    override fun resume() {
        TODO("Not yet implemented")
    }

    override fun endSession() {
        currentSession?.let {
            storage.updateSession(currentSession?.sessionId!!, Instant.now().toEpochMilli())

        }
        logger?.info("Session Ended:${currentSession?.sessionId}")

        currentSession = null;
    }

    override fun getSessions(): List<Session> {
        return storage.getAllSessionsWithEvents().map { sessionWithEvents ->
            val session = sessionWithEvents.session
            val events = sessionWithEvents.events.map { eventEntity ->

                var data = AnalyticsEvent(
                    eventName = eventEntity.eventName,
                    properties = jsonStringToMap(eventEntity.properties),
                    timestamp = eventEntity.timestamp
                )
                data
            }
            var resSession =
                AnalyticsSession(session.session_id, session.startTime, session.endTime)
            resSession.events.addAll(events)
            resSession
        }
    }

    override fun addEvent(eventName: String, properties: Map<String, Any>) {
        currentSession?.let {
            storage.saveEvent(
                AnalyticsEventEntity(
                    eventName = eventName,
                    sessionId = it.sessionId!!,
                    properties = gson.toJson(properties)
                )
            )
            logger?.info("Event added: $eventName with properties: $properties")

        }?: kotlin.run {
            throw IllegalStateException("Please initiate session first")
        }

    }


}