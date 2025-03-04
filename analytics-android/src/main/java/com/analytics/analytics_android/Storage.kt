package com.analytics.analytics_android

import com.analytics.analytics_android.core.storage.AnalyticsEventEntity
import com.analytics.analytics_android.core.storage.AnalyticsSessionEntity
import com.analytics.analytics_android.core.storage.SessionWithEvents

interface Storage {

    fun saveSession(session: AnalyticsSessionEntity);
    fun updateSession(sessionId: String, endTime: Long);
    fun getSessionPoolCount(): Int
    fun saveEvent(event: AnalyticsEventEntity)
    fun getEvents(sessionId: String): List<AnalyticsEventEntity>

    fun getSessionWithEvents(sessionId: String): SessionWithEvents?
    fun getSessionsWithEvents(limit: Int?): List<SessionWithEvents>
    fun removeAllSyncedData(ids:List<String>)
}