package com.analytics.analytics_android

import com.analytics.analytics_android.core.AnalyticsSession

interface Storage {
    fun saveSession(session: AnalyticsSession);
    fun getSessions(): List<Map<String, Any?>>;


}