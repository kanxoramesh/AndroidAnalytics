package com.analytics.analytics_android.utils

import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import com.analytics.analytics_android.Logger
import com.analytics.analytics_android.core.logger.AnalyticsLogger
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

val logger: Logger = AnalyticsLogger.with(LogLevel.DEBUG)
const val PERMISSION_CHECK_REPEAT_MAX_COUNT = 2


/** Controls the level of logging.  */
enum class LogLevel {
    /** No logging.  */
    NONE,

    /** Log exceptions only.  */
    INFO,

    /** Log exceptions and print debug output.  */
    DEBUG,

    /**
     * Log exceptions and print debug output.
     *
     */
    @Deprecated("Use {@link LogLevel#DEBUG} instead.")
    BASIC,

    /** Same as [LogLevel.DEBUG], and log transformations in bundled integrations.  */
    VERBOSE;

    fun log(): Boolean {
        return this != NONE
    }
}

private fun hasPermission(context: Context, permission: String, repeatCount: Int): Boolean {
    try {
        return context.checkCallingOrSelfPermission(permission) == PERMISSION_GRANTED
    } catch (e: Exception) {
        // exception during permission check means we need to assume it is not granted
        logger.error(e, "Exception during permission check")
        return (repeatCount < PERMISSION_CHECK_REPEAT_MAX_COUNT
                && hasPermission(context.applicationContext, permission, repeatCount + 1))
    }
}

fun jsonStringToMap(jsonString: String): Map<String, Any> {
    val gson = Gson()
    val mapType = object : TypeToken<Map<String, Any>>() {}.type
    return gson.fromJson(jsonString, mapType)
}