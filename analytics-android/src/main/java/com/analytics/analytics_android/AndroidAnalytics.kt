import android.content.Context
import com.analytics.analytics_android.Logger
import com.analytics.analytics_android.Session
import com.analytics.analytics_android.SessionController
import com.analytics.analytics_android.Storage
import com.analytics.analytics_android.core.logger.AnalyticsLogger
import com.analytics.analytics_android.core.session.SessionControllerImpl
import com.analytics.analytics_android.utils.LogLevel

/**
 * Main entry point for managing analytics sessions and events in an Android application.
 */
class AndroidAnalytics private constructor(
    private val builder: Builder
) {
    private var controller: SessionController? = null
    private var logger: Logger? = AnalyticsLogger.with(LogLevel.INFO)
    val storage = builder.getStorage() ?: throw IllegalStateException("Storage must be set before starting a session")

    init {
        logger = AnalyticsLogger.with(builder.getLogLevel());
        controller ?: SessionControllerImpl(logger, storage).also { controller = it }
    }

    companion object {
        private var instance: AndroidAnalytics? = null

        /**
         * Retrieves the singleton instance of AndroidAnalytics.
         * @throws IllegalStateException if AndroidAnalytics has not been initialized using initialize() method.
         */
        fun getInstance(): AndroidAnalytics {
            return instance
                ?: throw IllegalStateException("DefaultAnalyticsManager must be initialized using the Builder before getting the instance.")
        }

        /**
         * Initializes the AndroidAnalytics singleton instance.
         * @param builder Builder object containing configuration for AndroidAnalytics.
         */
        fun initialize(builder: Builder) {
            if (instance == null) {
                instance = builder.build()
            }
        }
    }

    /**
     * Starts a new analytics session.
     */
    fun startSession() {
        controller?.startSession()
    }


    /**
     * Adds an analytics event to the current session.
     * @param eventName The name or identifier of the event.
     * @param properties Additional properties associated with the event.
     */
    fun addEvent(eventName: String, properties: Map<String, Any>) {
        controller?.addEvent(eventName, properties)
    }

    /**
     * Ends the current analytics session.
     */
    fun endSession() {
        controller?.endSession();
    }

    /**
     * Retrieves a list of sessions with their associated data.
     * @return List of Session objects representing analytics sessions.
     */
    fun getSessionsData():List<Session>?{
        return controller?.getSessions()
    }


    /**
     * Builder class for configuring and creating an instance of AndroidAnalytics.
     * @param context The application context used for initialization.
     */
    class Builder(private val context: Context) {
        private var storage: Storage? = null
        private var logLevel: LogLevel? = null


        /**
         * Sets the storage implementation for storing analytics data.
         * @param storage Storage object to be used for analytics data storage.
         * @return Builder instance for method chaining.
         */
        fun setStorage(storage: Storage) = apply { this.storage = storage }
        fun getStorage(): Storage? {
            return storage
        }

        /**
         * Sets the log level for analytics logging.
         * @param logLevel LogLevel enum specifying the desired logging level.
         * @return Builder instance for method chaining.
         */
        fun setLogLevel(logLevel: LogLevel) = apply { this.logLevel = logLevel }
        fun getLogLevel(): LogLevel? {
            return logLevel
        }

        /**
         * Builds and returns an instance of AndroidAnalytics with the configured settings.
         * @return Configured instance of AndroidAnalytics.
         * @throws IllegalArgumentException if storage has not been set.
         */
        fun build(): AndroidAnalytics {
            if (storage == null) {
                throw IllegalArgumentException("Storage must be set")
            }
            return AndroidAnalytics(this)
        }
    }
}
