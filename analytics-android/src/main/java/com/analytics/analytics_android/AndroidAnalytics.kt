import android.content.Context
import android.util.Log
import com.analytics.analytics_android.AnalyticsEvent
import com.analytics.analytics_android.Logger
import com.analytics.analytics_android.Storage
import com.analytics.analytics_android.core.AnalyticsLogger
import com.analytics.analytics_android.core.AnalyticsSession
import com.analytics.analytics_android.utils.LogLevel

class AndroidAnalytics private constructor(
    private val builder: Builder
) {
    private var currentSession: AnalyticsSession? = null
    private var logger: Logger? = AnalyticsLogger.with(LogLevel.INFO)

    init {
        logger = AnalyticsLogger.with(builder.getLogLevel());
    }

    companion object {
        private var instance: AndroidAnalytics? = null

        fun getInstance(): AndroidAnalytics {
            return instance
                ?: throw IllegalStateException("DefaultAnalyticsManager must be initialized using the Builder before getting the instance.")
        }

        fun initialize(builder: Builder) {
            if (instance == null) {
                instance = builder.build()
            }
        }
    }

    fun startSession() {
        if (currentSession == null) {
            currentSession = AnalyticsSession()
            logger?.info("Session started: ${currentSession?.sessionId}")
        }
    }

    fun addEvent(eventName: String, properties: Map<String, Any>) {
        currentSession?.let {
            it.addEvent(AnalyticsEvent(eventName, properties))
            logger?.info("Event added: $eventName with properties: $properties")
        } ?: {
            startSession()
            currentSession?.addEvent(AnalyticsEvent(eventName, properties))
        }
    }

    fun endSession() {
        currentSession?.let {
            it.endSession()
            builder.getStorage()?.saveSession(it)
            logger?.info("Session ended: ${it.sessionId}")
            currentSession = null
        }
    }

    fun getSessionsData() {
        builder.getStorage()?.getSessions()
    }


    class Builder(private val context: Context) {
        private var storage: Storage? = null
        private var logLevel: LogLevel? = null


        fun setStorage(storage: Storage) = apply { this.storage = storage }
        fun getStorage(): Storage? {
            return storage
        }

        fun setLogLevel(logLevel: LogLevel) = apply { this.logLevel = logLevel }
        fun getLogLevel(): LogLevel? {
            return logLevel
        }

        fun build(): AndroidAnalytics {
            if (storage == null) {
                throw IllegalArgumentException("Storage must be set")
            }
            return AndroidAnalytics(this)
        }
    }
}
