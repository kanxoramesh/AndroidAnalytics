package com.analytics.analytics_android

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.BitmapFactory.Options
import androidx.lifecycle.Lifecycle
import com.analytics.analytics_android.utils.LogLevel
import com.analytics.analytics_android.utils.hasPermission
import com.analytics.analytics_android.utils.isEmptyOrBlank
import com.analytics.analytics_android.utils.isNullOrEmpty
import java.util.Collections
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class AndroidAnalytics private constructor(
    private val builder: AnalyticsBuilder
) {
    companion object {
         var instance: AndroidAnalytics? = null


        fun get(context: Context?): AndroidAnalytics {
            if (instance == null) {
                requireNotNull(context) { "Context must not be null." }
                synchronized(AndroidAnalytics::class.java) {
                    if (instance == null) {
                        val builder: AnalyticsBuilder = AnalyticsBuilder(context)

                        try {
                            val packageName = context.packageName
                            val flags =
                                context.packageManager
                                    .getApplicationInfo(packageName, 0)
                                    .flags
                            val debugging =
                                flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
                            if (debugging) {
                                builder.logLevel(LogLevel.INFO)
                            }
                        } catch (ignored: PackageManager.NameNotFoundException) {
                        }

                        instance = builder.build()
                    }
                }
            }
            return instance!!
        }

        /**
         * Set the global instance returned from [.get].
         */
        fun setSingletonInstance(analytics: AndroidAnalytics) {
            synchronized(AndroidAnalytics::class.java) {
                check(instance == null) { "Singleton instance already exists." }
                instance = analytics
            }
        }
    }

    class AnalyticsBuilder(internal val context: Context) {
        private var logLevel: LogLevel? = null
        private var application: Application? = null

        /**
         * Start building a new [Analytics] instance.
         *
         *
         * Throws [IllegalArgumentException] in following cases: - no INTERNET permission
         * granted - writeKey is empty
         */
        init{
            require(
                hasPermission(
                    context,
                    Manifest.permission.INTERNET
                )
            ) { "INTERNET permission is required." }
            application = context.applicationContext as Application
        }

        fun logLevel(logLevel: LogLevel?): AnalyticsBuilder {
            logLevel?.let {
                this.logLevel = logLevel;
            } ?: run {
                throw IllegalArgumentException("LogLevel must not be null.");
            }
            return this;
        }

        fun build(): AndroidAnalytics {

            if (logLevel == null) {
                logLevel = LogLevel.NONE
            }

            return AndroidAnalytics(
                this

            )
        }

    }


}