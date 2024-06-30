import android.Manifest
import android.content.Context
import com.analytics.analytics_android.Session
import com.analytics.analytics_android.SessionController
import com.analytics.analytics_android.Storage
import com.analytics.analytics_android.core.logger.AnalyticsLogger
import com.analytics.analytics_android.core.session.SessionControllerImpl
import com.analytics.analytics_android.network.HttpConnection
import com.analytics.analytics_android.network.HttpMethod
import com.analytics.analytics_android.NetworkSynchronizer
import com.analytics.analytics_android.R
import com.analytics.analytics_android.network.Executor
import com.analytics.analytics_android.network.Request
import com.analytics.analytics_android.utils.LogLevel
import com.analytics.analytics_android.utils.hasPermission
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient

/**
 * Main entry point for managing analytics sessions and events in an Android application.
 */
class AndroidAnalytics private constructor(
    private val builder: Builder
) {
    private val coroutineScope = CoroutineScope(Dispatchers.Default) // Example of creating a new coroutine scope
    private var defaultPoolCounter:Int=10
    private var controller: SessionController? = null
    val storage = builder.getStorage() ?: throw IllegalStateException("Storage must be set before starting a session")

    init {
       builder.getLogLevel()?.let {  AnalyticsLogger.initialize(it)}
        controller ?: SessionControllerImpl(storage).also { controller = it }
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
        controller?.startSession(builder.getMaxSessionPoolCount()?:defaultPoolCounter){isReadyToSync,sessions:List<Session>?->
            if(isReadyToSync){
                sessions?.let {
                coroutineScope.launch {
                    var result=builder.getNetworkSynchronizer()?.sendRequest(Request(sessions))
                    Executor.shutdown()
                    if(result?.isSuccessful == true){
                        controller?.removedSyncedData(result.sessionIds)
                        coroutineScope?.cancel()
                    }}
                }
            }

        }
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
        return controller?.getSessions(null)
    }


    /**
     * Builder class for configuring and creating an instance of AndroidAnalytics.
     * @param context The application context used for initialization.
     */
    class Builder( val context: Context) {
        private var storage: Storage? = null
        private var logLevel: LogLevel? = null
        private var poolCount: Int? = 10
        private var networkSynchronizer: NetworkSynchronizer?=null

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
        fun setLogLevel(logLevel: LogLevel?) = apply { this.logLevel = logLevel }
        fun getLogLevel(): LogLevel? {
            return logLevel
        }

        /**
         * Sync cache data  to network
         */
        fun setNetworkSynchronizer(networkSynchronizer: NetworkSynchronizer) = apply {

            if (!hasPermission(context, Manifest.permission.INTERNET))
            {
                AnalyticsLogger.error(Exception(),"Android manifest permission issue: %s","INTERNET")
                throw Exception(context.getString(R.string.permission_error))
            }

            this.networkSynchronizer = networkSynchronizer
        }
        fun getNetworkSynchronizer(): NetworkSynchronizer? {
            return networkSynchronizer
        }

        /**
         * Max session stored in local cache, default is 10. Network sync will call after that
         */
        fun maxSessionPoolCount(count: Int) = apply { this.poolCount = count }
        fun getMaxSessionPoolCount(): Int? {
            return poolCount
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


    /**
     * Builder for the OkHttpNetworkConnection.
     * @param uri The uri of the collector
     */
    class OkHttpNetworkConnectionBuilder(
        val uri: String) {
        var httpMethod = HttpMethod.POST // Optional
        var timeout = 30 // Optional
        var client: OkHttpClient? = null // Optional
        var requestHeaders: Map<String, String>? = null // Optional

        /**
         * GET or POST.
         * @param httpMethod The method
         * @return itself
         */
        fun method(httpMethod: HttpMethod): OkHttpNetworkConnectionBuilder {
            this.httpMethod = httpMethod
            return this
        }

        /**
         * Time-out to wait for sync execution in SECOND
         */
        fun timeout(timeout: Int): OkHttpNetworkConnectionBuilder {
            this.timeout = timeout
            return this
        }

        fun client(client: OkHttpClient?): OkHttpNetworkConnectionBuilder {
            this.client = client
            return this
        }

        /**
         * A map of custom HTTP headers to add to the request.
         */
        fun requestHeaders(requestHeaders: Map<String, String>?): OkHttpNetworkConnectionBuilder {
            this.requestHeaders = requestHeaders
            return this
        }

        /**
         * Creates a new OkHttpConnection
         *
         * @return a new OkHttpConnection object
         */
        fun build(): NetworkSynchronizer {
            return HttpConnection(this)
        }
    }


}
