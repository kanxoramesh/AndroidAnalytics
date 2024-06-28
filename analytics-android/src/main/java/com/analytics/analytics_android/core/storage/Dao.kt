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

    @Query("SELECT * FROM sessions")
    fun getSessions(): List<AnalyticsSessionEntity>

    @Query("UPDATE sessions SET endTime = :endTime WHERE session_id = :sessionId")
    fun updateSessionEndTime(sessionId: String, endTime: Long)

    @Transaction
    @Query("SELECT * FROM sessions WHERE session_id = :sessionId")
    fun getSessionWithEvents(sessionId: String): SessionWithEvents?

    @Transaction
    @Query("SELECT * FROM sessions")
    fun getAllSessionsWithEvents(): List<SessionWithEvents>
}

