import android.content.Context
import com.analytics.analytics_android.AnalyticsEvent
import com.analytics.analytics_android.Logger
import com.analytics.analytics_android.Session
import com.analytics.analytics_android.Storage
import com.analytics.analytics_android.core.logger.AnalyticsLogger
import com.analytics.analytics_android.core.session.AnalyticsSession
import com.analytics.analytics_android.core.session.SessionController
import com.analytics.analytics_android.utils.LogLevel

class AndroidAnalytics private constructor(
    private val builder: Builder
) {
    private var controller: SessionController? = null
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
        val storage = builder.getStorage() ?: throw IllegalStateException("Storage must be set before starting a session")
        if (controller == null) {
            controller = SessionController(logger, storage)
        } else {
            controller?.startSession()
        }
    }

    fun addEvent(eventName: String, properties: Map<String, Any>) {
        val controller = controller ?: throw IllegalStateException("Please initiate session first")
        controller.addEvent(eventName, properties)
    }

    fun endSession() {
        controller?.endSession();
    }

    fun getSessionsData():List<Session>?{
        return controller?.getSessions()
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
