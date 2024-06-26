package com.analytics.analytics_android.core


import android.util.Log
import com.analytics.analytics_android.Logger
import com.analytics.analytics_android.utils.LogLevel

/** An abstraction for logging messages.  */
class AnalyticsLogger(private val tag: String, val logLevel: LogLevel) : Logger {
    /** Log a verbose message.  */
    override fun verbose(format: String?, vararg extra: Any?) {
        if (shouldLog(LogLevel.VERBOSE)) {
            Log.v(tag, String.format(format!!, *extra))
        }
    }

    /** Log an info message.  */
    override fun info(format: String?, vararg extra: Any?) {
        if (shouldLog(LogLevel.INFO)) {
            Log.i(tag, String.format(format!!, *extra))
        }
    }

    /** Log a debug message.  */
    override fun debug(format: String?, vararg extra: Any?) {
        if (shouldLog(LogLevel.DEBUG)) {
            Log.d(tag, String.format(format!!, *extra))
        }
    }

    /** Log an error message.  */
    override fun error(error: Throwable?, format: String?, vararg extra: Any?) {
        if (shouldLog(LogLevel.INFO)) {
            Log.e(tag, String.format(format!!, *extra), error)
        }
    }

    private fun shouldLog(level: LogLevel): Boolean {
        return logLevel.ordinal >= level.ordinal
    }

    companion object {
        private const val DEFAULT_TAG = "Analytics"

        /** Returns a new [Logger] with the give `level`.  */
        fun with(level: LogLevel?): Logger {
            return AnalyticsLogger(DEFAULT_TAG, level ?: LogLevel.INFO)

        }
    }
}