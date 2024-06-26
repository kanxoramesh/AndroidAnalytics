package com.analytics.analytics_android

interface Logger {
    fun verbose(format: String?, vararg extra: Any?)
    fun debug(format: String?, vararg extra: Any?)
    fun info(format: String?, vararg extra: Any?)
    fun error(error: Throwable?, format: String?, vararg extra: Any?)
}