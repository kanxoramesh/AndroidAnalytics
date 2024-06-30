package com.example.myapplication

import AndroidAnalytics
import android.app.Application
import com.analytics.analytics_android.core.storage.RoomAnalyticsStorage
import com.analytics.analytics_android.network.HttpMethod
import com.analytics.analytics_android.utils.LogLevel

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AndroidAnalytics.initialize(
            AndroidAnalytics.Builder(this)
                .setStorage(RoomAnalyticsStorage(this))
                .setLogLevel(LogLevel.INFO)
                .maxSessionPoolCount(5)
                .setNetworkSynchronizer(AndroidAnalytics.OkHttpNetworkConnectionBuilder("http://100.115.92.195:8080/network")
                    .method(HttpMethod.POST)
                    .timeout(5)
                    //.client(OkHttpClient())//Optional
                    .build()))
    }
}