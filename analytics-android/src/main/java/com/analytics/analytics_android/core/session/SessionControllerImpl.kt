package com.analytics.analytics_android.core.session

import com.analytics.analytics_android.AnalyticsEvent
import com.analytics.analytics_android.Session
import com.analytics.analytics_android.SessionController
import com.analytics.analytics_android.Storage
import com.analytics.analytics_android.core.logger.AnalyticsLogger
import com.analytics.analytics_android.core.storage.AnalyticsEventEntity
import com.analytics.analytics_android.core.storage.AnalyticsSessionEntity
import com.analytics.analytics_android.utils.jsonStringToMap
import com.google.gson.Gson
import java.time.Instant

/**
 * Implementation of SessionController interface for managing analytics sessions and events.
 * @param logger Logger instance for logging session and event actions.
 * @param storage Storage instance for saving and retrieving session and event data.
 */
class SessionControllerImpl(private val storage: Storage) :
    SessionController {
    private val gson = Gson()
    private var currentSessionInternal: Session? = null

    override var currentSession: Session?
        get() = currentSessionInternal
        set(value) {
            currentSessionInternal = value
        }

    /**
     * Starts a new session if one does not already exist.
     * If a session is started, it is saved to storage and logged.
     */
    private fun startSessionIfNeeded() {
        if (currentSession == null) {
            currentSession = AnalyticsSession()
            currentSession?.let {
                storage.saveSession(AnalyticsSessionEntity(it.sessionId!!, it.startTime!!))
                AnalyticsLogger.info("Session started: ${it.sessionId}")
            }

        } else {
            AnalyticsLogger.info("Session already created: ${currentSession?.sessionId}")
        }

    }

    /**
     * Starts a session with the specified pool count and returns the result via the callback.
     *
     * @param poolCount The desired pool count for sessions.
     * @param param Callback function that receives a boolean indicating readiness and an optional list of sessions.
     */
    override fun startSession(poolCount: Int, param: (Boolean, List<Session>?) -> Unit) {
        startSessionIfNeeded()
        val isReady = storage.getSessionPoolCount()>poolCount
        var list: List<Session>? = null
        if (isReady) {
            list = getSessions(poolCount)
        }
        param(isReady, list)
    }

    override fun pause() {
        TODO("Not yet implemented")
    }

    /**
     * Removes synchronized Session and Event data for the specified IDs from storage.
     *
     * @param ids List of IDs identifying data to be removed.
     */
    override fun removedSyncedData(ids:List<String>) {
        storage.removeAllSyncedData(ids)
    }

    override fun resume() {
        TODO("Not yet implemented")
    }

    /**
     * Ends the current session by updating its end time in storage and logging the action.
     */
    override fun endSession() {
        currentSession?.let {
            storage.updateSession(currentSession?.sessionId!!, Instant.now().toEpochMilli())

        }
        AnalyticsLogger.info("Session Ended:${currentSession?.sessionId}")

        currentSession = null;
    }


    /**
     * Retrieves all sessions from storage along with their associated events.
     * @return List of Session objects, each containing a list of associated AnalyticsEvent objects.
     */
    override fun getSessions(limit: Int?): List<Session> {
        return storage.getSessionsWithEvents(limit).map { sessionWithEvents ->
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

    /**
     * Adds an event to the current session.
     * If no session is active, throws IllegalStateException.
     * @param eventName The name or identifier of the event.
     * @param properties Additional properties associated with the event.
     * @throws IllegalStateException if no session is active when adding the event.
     */
    override fun addEvent(eventName: String, properties: Map<String, Any>) {
        currentSession?.let {
            storage.saveEvent(
                AnalyticsEventEntity(
                    eventName = eventName,
                    sessionId = it.sessionId!!,
                    properties = gson.toJson(properties)
                )
            )
            AnalyticsLogger.info("Event added: $eventName with properties: $properties")

        } ?: kotlin.run {
            AnalyticsLogger.info("Session not created")

            throw IllegalStateException("Please initiate session first")
        }

    }


}