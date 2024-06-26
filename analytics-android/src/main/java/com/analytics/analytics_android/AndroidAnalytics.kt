import com.analytics.analytics_android.AnalyticsEvent
import com.analytics.analytics_android.AnalyticsSession
import com.analytics.analytics_android.AnalyticsStorage

class AndroidAnalytics private constructor(
    private val storage: AnalyticsStorage
) {
    private var currentSession: AnalyticsSession? = null

    fun startSession() {
        if (currentSession == null) {
            currentSession = AnalyticsSession()
            println("Session started: ${currentSession?.sessionId}")
        }
    }

    fun endSession() {
        currentSession?.let {
            it.endSession()
            storage.saveSession(it)
            println("Session ended: ${it.sessionId}")
            currentSession = null
        }
    }

    fun addEvent(eventName: String, properties: Map<String, Any>) {
        currentSession?.let {
            val event = AnalyticsEvent(eventName, properties)
            it.addEvent(event)
            println("Event added: $eventName with properties: $properties")
        }
    }

    class Builder {
        private var storage: AnalyticsStorage? = null

        fun setStorage(storage: AnalyticsStorage) = apply { this.storage = storage }

        fun build(): AndroidAnalytics {
            if (storage == null) {
                throw IllegalArgumentException("Storage must be set")
            }
            return AndroidAnalytics(storage!!)
        }
    }
}
