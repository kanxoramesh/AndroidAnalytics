package com.analytics.analytics_android.utils

import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import com.analytics.analytics_android.core.logger.AnalyticsLogger
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

const val PERMISSION_CHECK_REPEAT_MAX_COUNT = 2

enum class LogLevel {
    NONE,
    INFO,
    DEBUG;
}

 fun hasPermission(context: Context, permission: String): Boolean {
    try {
        return context.checkCallingOrSelfPermission(permission) == PERMISSION_GRANTED
    } catch (e: Exception) {
        AnalyticsLogger.error(e, "Exception during permission check. Give Internet permission")
        return false
    }
}

fun jsonStringToMap(jsonString: String): Map<String, Any> {
    val gson = Gson()
    val mapType = object : TypeToken<Map<String, Any>>() {}.type
    return gson.fromJson(jsonString, mapType)
}