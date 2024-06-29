package com.analytics.analytics_android

import com.analytics.analytics_android.core.logger.AnalyticsLogger
import com.analytics.analytics_android.core.session.SessionControllerImpl
import com.analytics.analytics_android.core.storage.AnalyticsEventEntity
import com.analytics.analytics_android.core.storage.AnalyticsSessionEntity
import com.analytics.analytics_android.core.storage.SessionWithEvents
import com.google.gson.Gson
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.time.Instant

class SessionControllerImplTest {


    @Mock
    private lateinit var mockStorage: Storage

    private lateinit var sessionController: SessionControllerImpl

    private val gson = Gson()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        sessionController = SessionControllerImpl(mockStorage)
    }

    @Test
    fun testStartAndEndSession() {

        // Start session
        sessionController.startSession(10) { _,_ ->

        }

        // Verify session started and saved to storage
        assertNotNull(sessionController.currentSession)
        val sessionId = sessionController.currentSession?.sessionId

        val mockSessionEntity = AnalyticsSessionEntity(sessionId!!, sessionController.currentSession?.startTime!!)

        verify(mockStorage).saveSession(mockSessionEntity) // Verify saveSession was called with any argument
        // End session
        sessionController.endSession()

        // Verify session ended and updated in storage
        assertNull(sessionController.currentSession)
    }

    @Test
    fun testAddEvent() {
        // Mock session and event data
        val sessionId = "testSessionId"
        val eventName = "testEvent"
        val properties = mapOf("key" to "value")
        val eventEntity = AnalyticsEventEntity(
            121L,
            sessionId,
            eventName,
            gson.toJson(properties),
            Instant.now().toEpochMilli()
        )
        mockStorage.saveEvent(eventEntity)

        // Start session
        sessionController.startSession(10) { _ ,_->

        }

        // Add event
        sessionController.addEvent(eventName, properties)
        verify(mockStorage).saveEvent(eventEntity)
    }

    @Test
    fun testGetSessions() {
        // Mock session with events data
        val sessionId = "testSessionId"
        val startTime = Instant.now().toEpochMilli()
        val endTime = startTime + 1000L
        val sessionEntity = AnalyticsSessionEntity(sessionId, startTime, endTime)
        val eventEntity = AnalyticsEventEntity(
            121L,
            sessionId,
            "eventname",
            gson.toJson(mapOf("key" to "value")),
            Instant.now().toEpochMilli()
        )
        val sessionWithEvents = SessionWithEvents(sessionEntity, listOf(eventEntity))

        `when`(mockStorage.getSessionsWithEvents(null)).thenReturn(listOf(sessionWithEvents))

        // Retrieve sessions
        val sessions = sessionController.getSessions(null)
        assertEquals(1, sessions.size)

        // Verify session data
        val retrievedSession = sessions[0]
        assertEquals(sessionId, retrievedSession.sessionId)
        assertEquals(startTime, retrievedSession.startTime)
        assertEquals(endTime, retrievedSession.endTime)

        // Verify event data
        assertEquals(1, retrievedSession.events.size)
        val retrievedEvent = retrievedSession.events[0]
        assertEquals("eventname", retrievedEvent.getEventData()["eventName"])
    }
}
