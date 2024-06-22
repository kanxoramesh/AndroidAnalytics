package com.analytics.analytics_android.utils


import android.util.Log

/** An abstraction for logging messages.  */
class Logger(private val tag: String, val logLevel: LogLevel) {
    /** Log a verbose message.  */
    fun verbose(format: String?, vararg extra: Any?) {
        if (shouldLog(LogLevel.VERBOSE)) {
            Log.v(tag, String.format(format!!, *extra))
        }
    }

    /** Log an info message.  */
    fun info(format: String?, vararg extra: Any?) {
        if (shouldLog(LogLevel.INFO)) {
            Log.i(tag, String.format(format!!, *extra))
        }
    }

    /** Log a debug message.  */
    fun debug(format: String?, vararg extra: Any?) {
        if (shouldLog(LogLevel.DEBUG)) {
            Log.d(tag, String.format(format!!, *extra))
        }
    }

    /** Log an error message.  */
    fun error(error: Throwable?, format: String?, vararg extra: Any?) {
        if (shouldLog(LogLevel.INFO)) {
            Log.e(tag, String.format(format!!, *extra), error)
        }
    }

    /**
     * Returns a new [Logger] with the same `level` as this one and the given `tag`.
     */
    fun subLog(tag: String): Logger {
        return Logger(DEFAULT_TAG + "-" + tag, logLevel)
    }

    private fun shouldLog(level: LogLevel): Boolean {
        return logLevel.ordinal >= level.ordinal
    }

    companion object {
        private const val DEFAULT_TAG = "Analytics"

        /** Returns a new [Logger] with the give `level`.  */
        fun with(level: LogLevel): Logger {
            return Logger(DEFAULT_TAG, level)
        }
    }
}
