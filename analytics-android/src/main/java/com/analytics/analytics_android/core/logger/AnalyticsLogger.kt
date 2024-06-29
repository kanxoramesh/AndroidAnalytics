package com.analytics.analytics_android.core.logger


import android.util.Log
import androidx.annotation.RestrictTo
import com.analytics.analytics_android.utils.LogLevel

@RestrictTo(RestrictTo.Scope.LIBRARY)
object AnalyticsLogger  {

     fun info(format: String?, vararg extra: Any?) {
        if (shouldLog(LogLevel.INFO)) {
            Log.i(DEFAULT_TAG, String.format(format!!, *extra))
        }
    }

     fun debug(format: String?, vararg extra: Any?) {
        if (shouldLog(LogLevel.DEBUG)) {
            Log.d(DEFAULT_TAG, String.format(format!!, *extra))
        }
    }

     fun error(error: Throwable?, format: String?, vararg extra: Any?) {
        if (shouldLog(LogLevel.INFO)) {
            Log.e(DEFAULT_TAG, String.format(format!!, *extra), error)
        }
    }

    private fun shouldLog(level: LogLevel): Boolean {
        return logLevel.ordinal >= level.ordinal
    }

    private var logLevel: LogLevel=LogLevel.NONE
    private const val DEFAULT_TAG = "Analytics"

    fun initialize(logLevel: LogLevel) {
        this.logLevel = logLevel
    }



}