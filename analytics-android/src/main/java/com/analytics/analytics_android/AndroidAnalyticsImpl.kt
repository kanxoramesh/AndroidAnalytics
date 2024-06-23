package com.analytics.analytics_android

import android.Manifest
import android.app.Application
import android.content.Context
import com.analytics.analytics_android.utils.hasPermission


class AndroidAnalyticsImpl private constructor(
    private val builder: AnalyticsBuilder
) : AndroidAnalytics {
    companion object {
        private var instance: AndroidAnalytics? = null


        fun get(context: Context?): AndroidAnalytics {
            if (instance == null) {
                requireNotNull(context) { "Context must not be null." }
                synchronized(AndroidAnalytics::class.java) {
                    if (instance == null) {
                        val builder: AnalyticsBuilder = AnalyticsBuilder(context)
                        instance = builder.build()
                    }
                }
            }
            return instance!!
        }

        /**
         * Set the global instance returned from [.get].
         */
        fun getInstance(analytics: AndroidAnalytics) {
            synchronized(AndroidAnalytics::class.java) {
                check(instance == null) { "Singleton instance already exists. Please use AndroidAnalytics.get(context)" }
                instance = analytics
            }
        }
    }

    override fun logEvent(analyticsEvent: AnalyticsEvent) {

    }


    class AnalyticsBuilder(internal val context: Context) {
        private var application: Application? = null

        /**
         * Start building a new [AndroidAnalytics] instance.
         * Throws [IllegalArgumentException] in following cases: - no INTERNET permission
         */
        init {
            require(
                hasPermission(
                    context,
                    Manifest.permission.INTERNET
                )
            ) { "INTERNET permission is required." }
            application = context.applicationContext as Application
        }


        fun build(): AndroidAnalytics {

            return AndroidAnalyticsImpl(
                this

            )
        }

    }


}