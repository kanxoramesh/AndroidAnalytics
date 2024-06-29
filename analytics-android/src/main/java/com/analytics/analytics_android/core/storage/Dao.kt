package com.analytics.analytics_android.core.storage

import androidx.room.*

@Dao
interface AnalyticsEventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEvent(event: AnalyticsEventEntity)

    @Query("SELECT * FROM events WHERE session_id = :sessionId")
    fun getEventsForSession(sessionId: String): List<AnalyticsEventEntity>
}

@Dao
interface AnalyticsSessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSession(session: AnalyticsSessionEntity)

    @Query("SELECT * FROM sessions WHERE session_id = :sessionId")
    fun getSession(sessionId: String): AnalyticsSessionEntity?

    @Query("SELECT * FROM sessions ORDER BY startTime")
    fun getSessions(): List<AnalyticsSessionEntity>

    @Query("SELECT COUNT(*) FROM sessions")
    fun getSessionCount(): Int

    @Query("UPDATE sessions SET endTime = :endTime WHERE session_id = :sessionId")
    fun updateSessionEndTime(sessionId: String, endTime: Long)

    @Transaction
    @Query("SELECT * FROM sessions WHERE session_id = :sessionId")
    fun getSessionWithEvents(sessionId: String): SessionWithEvents?

    @Transaction
    @Query("SELECT * FROM sessions ORDER BY startTime")
    fun getAllSessionsWithEvents(): List<SessionWithEvents>

    @Query("SELECT * FROM sessions ORDER BY startTime LIMIT :count ")
    fun getFirstLimitSession(count:Int): List<SessionWithEvents>

    @Query("DELETE FROM sessions WHERE session_id IN (:ids)")
    fun deleteSessionsByIds(ids: List<String>)

    @Query("DELETE FROM events WHERE session_id IN (:sessionIds)")
    fun deleteEventsBySessionIds(sessionIds: List<String>)

    @Transaction
    fun deleteSessionsWithEvents(sessionIds: List<String>) {
        deleteEventsBySessionIds(sessionIds)
        deleteSessionsByIds(sessionIds)
    }
}

