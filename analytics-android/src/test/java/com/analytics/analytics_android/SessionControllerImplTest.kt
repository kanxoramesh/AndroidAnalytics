package com.analytics.analytics_android

import android.util.Log
import androidx.lifecycle.MethodCallsLogger
import com.analytics.analytics_android.core.logger.AnalyticsLogger
import com.analytics.analytics_android.core.session.AnalyticsSession
import com.analytics.analytics_android.core.session.SessionControllerImpl
import com.analytics.analytics_android.core.storage.AnalyticsEventEntity
import com.analytics.analytics_android.core.storage.AnalyticsSessionEntity
import com.analytics.analytics_android.core.storage.SessionWithEvents
import com.google.gson.Gson
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.mockito.Mock
import org.mockito.MockedStatic
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.time.Instant
import java.util.logging.Logger

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
    fun `test startSession when session is ready`() {
          sessionController = spy(SessionControllerImpl(mockStorage))
        val logMock: MockedStatic<Log> = mockStatic(Log::class.java)

        logMock.`when`<Int> { Log.i(any<String>(), anyString()) }.thenReturn(0)

        // Mock dependencies
        doAnswer { null }.`when`(sessionController).invokePrivateMethod<Unit>("startSessionIfNeeded")
        doReturn(6).`when`(mockStorage).getSessionPoolCount()
        val sessions = listOf(AnalyticsSession(), AnalyticsSession())
        doReturn(sessions).`when`(sessionController).getSessions(5)

        var resultIsReady: Boolean? = null
        var resultList: List<Session>? = null

        // Call the method under test
        sessionController.startSession(5) { isReady, list ->
            resultIsReady = isReady
            resultList = list
        }

        // Verify behavior
        verify(sessionController).getSessions(5)

        // Assert results
        assertEquals(true, resultIsReady)
        assertEquals(sessions, resultList)
    }
    @Test
    fun testStartAndEndSession() {

        var resultIsReady: Boolean? = null
        var resultList: List<Session>? = null

        // Call the method under test
        sessionController.startSession(5) { isReady, list ->
            resultIsReady = isReady
            resultList = list
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
        Mockito.`when`(mockStorage.saveEvent(eventEntity)).thenAnswer { invocation ->
            val savedEvent = invocation.arguments[0] as AnalyticsEventEntity
            // Perform assertions on savedEvent if needed
            assertEquals(eventEntity.eventName, savedEvent.eventName)
            assertEquals(eventEntity.sessionId, savedEvent.sessionId)
            assertEquals(eventEntity.properties, savedEvent.properties)
            // You can add more assertions here as needed

            null // Stubbed method doesn't return anything useful
        }
        mockStorage.saveEvent(eventEntity)



        // Start session
        sessionController.startSession(10) { _, _ -> }


        // Add event
        sessionController.addEvent(eventName, properties)
        verify(mockStorage).saveEvent(eventEntity)
    }

    @Test
    fun testGetSessions() {
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

        // Mock behavior of storage.getSessionPoolCount()
        Mockito.`when`(mockStorage.getSessionPoolCount()).thenReturn(5)

        // Mock behavior of getSessions() if needed
        Mockito.`when`(mockStorage.saveEvent(eventEntity)).then {
            val savedEvent = it.arguments[0] as AnalyticsEventEntity
            // Perform assertions on savedEvent if needed
            assertEquals(eventEntity.eventName, savedEvent.eventName)
            assertEquals(eventEntity.sessionId, savedEvent.sessionId)
            assertEquals(eventEntity.properties, savedEvent.properties)
            // You can add more assertions here as needed

            null // Stubbed method doesn't return anything useful
        }

        // Call startSession on sessionController
        sessionController.startSession(10) { isReady, sessions ->
            // Assertions on the lambda parameters if needed
            assertFalse(isReady)
            assertNull(sessions) // Since list is null in your implementation when isReady is true
        }

      }

    private fun <T> Any.invokePrivateMethod(methodName: String, vararg args: Any): T {
        val method = this::class.java.getDeclaredMethod(methodName, *args.map { it::class.java }.toTypedArray())
        method.isAccessible = true
        return method.invoke(this, *args) as T
    }
}
