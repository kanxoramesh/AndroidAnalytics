package com.analytics.analytics_android.utils

import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.text.TextUtils
import android.text.TextUtils.getTrimmedLength
import androidx.annotation.Nullable


val logger: Logger = Logger.with(LogLevel.DEBUG)
const val PERMISSION_CHECK_REPEAT_MAX_COUNT = 2

fun getResourceString(context: Context, key: String): String? {
    val id = getIdentifier(context, "string", key)
    return if (id != 0) {
        context.resources.getString(id)
    } else {
        null
    }
}

/** Get the identifier for the resource with a given type and key.  */
private fun getIdentifier(context: Context, type: String, key: String): Int {
    return context.resources.getIdentifier(key, type, context.packageName)
}

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

fun hasPermission(context: Context, permission: String): Boolean {
    return hasPermission(context, permission, 0)
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

/**
 * Returns true if the string is empty (once trimmed).
 *
 * @param str the string to be examined
 * @return true if the string is empty (once trimmed) or 0-length
 */
fun isEmptyOrBlank(str: CharSequence): Boolean {
    return str.length == 0 || getTrimmedLength(str) == 0
}

/** Returns true if the string is null, or empty (once trimmed).  */
fun isNullOrEmpty(text: CharSequence?): Boolean {
    return isEmpty(text) || getTrimmedLength(text) == 0
}

/**
 * Returns true if the string is null or 0-length.
 *
 *
 * Copied from [TextUtils.isEmpty]
 *
 * @param str the string to be examined
 * @return true if str is null or zero length
 */
fun isEmpty(str: CharSequence?): Boolean {
    return str == null || str.length == 0
}