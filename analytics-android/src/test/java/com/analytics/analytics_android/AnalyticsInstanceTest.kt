package com.analytics.analytics_android

import AndroidAnalytics
import android.content.Context
import com.analytics.analytics_android.utils.LogLevel
import org.junit.Test

import org.junit.Assert.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class AndroidAnalyticsTest {
    private lateinit var mockContext: Context
    private lateinit var mockStorage: Storage
    private lateinit var mockLogger: Logger

    @Before
    fun setUp() {

        mockContext = mock(Context::class.java)
        mockStorage = mock(Storage::class.java)
        mockLogger = mock(Logger::class.java)

    }



    @Test
    fun testGetInstance() {
        // Create a builder for initializing AndroidAnalytics
        val builder =
            AndroidAnalytics.Builder(mockContext).setStorage(mockStorage).setLogLevel(LogLevel.INFO)

        AndroidAnalytics.initialize(builder)

        // Retrieve the singleton instance
        val analytics = AndroidAnalytics.getInstance()

        // Check that the instance is not null
        assertNotNull(analytics)

        // Check that the instance is the same when retrieved again
        assertThat(analytics).isEqualTo(AndroidAnalytics.getInstance())

    }

    @Test
    fun testStartSession() {
        // Initialize AndroidAnalytics
        val builder = AndroidAnalytics.Builder(mockContext)
            .setStorage(mockStorage)
            .setLogLevel(LogLevel.INFO)
        AndroidAnalytics.initialize(builder)

        // Retrieve the singleton instance
        val analytics = AndroidAnalytics.getInstance()

        // Mock the SessionController
        val mockController = mock(SessionController::class.java)
        val controllerField = AndroidAnalytics::class.java.getDeclaredField("controller")
        controllerField.isAccessible = true
        controllerField.set(analytics, mockController)

        // Start a session and verify the startSession method is called on the controller
        analytics.startSession()
        verify(mockController).startSession(builder.getMaxSessionPoolCount()?:10) { isReady,list ->

        }
    }

    @Test
    fun testAddEvent() {
        // Initialize AndroidAnalytics
        val builder = AndroidAnalytics.Builder(mockContext)
            .setStorage(mockStorage)
            .setLogLevel(LogLevel.INFO)
        AndroidAnalytics.initialize(builder)

        // Retrieve the singleton instance
        val analytics = AndroidAnalytics.getInstance()

        // Mock the SessionController
        val mockController = mock(SessionController::class.java)
        val controllerField = AndroidAnalytics::class.java.getDeclaredField("controller")
        controllerField.isAccessible = true
        controllerField.set(analytics, mockController)

        // Add an event and verify the addEvent method is called on the controller
        val eventName = "testEvent"
        val properties = mapOf("key" to "value")
        analytics.addEvent(eventName, properties)
        verify(mockController).addEvent(eventName, properties)
    }
    @Test
    fun testEndSession() {
        // Initialize AndroidAnalytics
        val builder = AndroidAnalytics.Builder(mockContext)
            .setStorage(mockStorage)
            .setLogLevel(LogLevel.INFO)
        AndroidAnalytics.initialize(builder)

        // Retrieve the singleton instance
        val analytics = AndroidAnalytics.getInstance()

        // Mock the SessionController
        val mockController = mock(SessionController::class.java)
        val controllerField = AndroidAnalytics::class.java.getDeclaredField("controller")
        controllerField.isAccessible = true
        controllerField.set(analytics, mockController)

        // End a session and verify the endSession method is called on the controller
        analytics.endSession()
        verify(mockController).endSession()
    }
    @Test
    fun testGetSessionsData() {
        // Initialize AndroidAnalytics
        val builder = AndroidAnalytics.Builder(mockContext)
            .setStorage(mockStorage)
            .setLogLevel(LogLevel.INFO)
        AndroidAnalytics.initialize(builder)

        // Retrieve the singleton instance
        val analytics = AndroidAnalytics.getInstance()

        // Mock the SessionController and its return value
        val mockController = mock(SessionController::class.java)
        val sessions = listOf<Session>(mock(Session::class.java))
        `when`(mockController.getSessions(null)).thenReturn(sessions)

        // Inject the mock controller into the analytics instance
        val controllerField = AndroidAnalytics::class.java.getDeclaredField("controller")
        controllerField.isAccessible = true
        controllerField.set(analytics, mockController)

        // Get sessions data and verify the method is called on the controller
        val result = analytics.getSessionsData()
        verify(mockController).getSessions(null)
        assertThat(result).isEqualTo(sessions)
    }
}