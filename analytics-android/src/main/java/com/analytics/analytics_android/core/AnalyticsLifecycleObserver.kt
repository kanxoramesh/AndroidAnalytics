package com.analytics.analytics_android.core
import AndroidAnalytics
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class AnalyticsLifecycleObserver(
    private val analyticsManager: AndroidAnalytics
) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        analyticsManager.endSession()
    }

}
