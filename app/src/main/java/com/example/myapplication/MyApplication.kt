package com.example.myapplication

import AndroidAnalytics
import android.app.Application
import com.analytics.analytics_android.utils.LogLevel

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AndroidAnalytics.initialize(
            AndroidAnalytics.Builder(this)
                .setStorage(AnalyticsStorage(this))
                .setLogLevel(LogLevel.INFO)
        )
    }
}