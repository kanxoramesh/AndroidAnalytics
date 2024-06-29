package com.analytics.analytics_android.core.storage

import android.content.Context
import androidx.room.Room
import com.analytics.analytics_android.Storage

class RoomAnalyticsStorage(context: Context) : Storage {

    private val db: AnalyticsDatabase = Room.databaseBuilder(
        context.applicationContext,
        AnalyticsDatabase::class.java, "analytics-database"
    ).fallbackToDestructiveMigration()
        .allowMainThreadQueries()
        .build()

    private val eventDao = db.analyticsEventDao()
    private val sessionDao = db.analyticsSessionDao()


    override fun saveSession(session: AnalyticsSessionEntity) {
        sessionDao.insertSession(session)
    }

    override fun updateSession(sessionId: String, endTime: Long) {
        sessionDao.updateSessionEndTime(sessionId, endTime)
    }


    override fun getSessionPoolCount(): Int {
        return sessionDao.getSessionCount()

    }

    override fun removeAllSyncedData(ids: List<String>) {
        return sessionDao.deleteSessionsWithEvents(ids);
    }

    override fun saveEvent(event: AnalyticsEventEntity) {
        eventDao.insertEvent(event)
    }

    override fun getEvents(sessionId: String): List<AnalyticsEventEntity> {
        return eventDao.getEventsForSession(sessionId)
    }

    override fun getSessionWithEvents(sessionId: String): SessionWithEvents? {
        return sessionDao.getSessionWithEvents(sessionId)
    }

    override fun getSessionsWithEvents(limit: Int?): List<SessionWithEvents> {
        limit?.let {
            return sessionDao.getFirstLimitSession(limit)
        } ?: run {
            return sessionDao.getAllSessionsWithEvents()
        }
    }


}
