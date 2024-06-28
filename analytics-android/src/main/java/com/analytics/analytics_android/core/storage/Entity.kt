package com.analytics.analytics_android.core.storage

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.google.gson.Gson
import java.time.Instant

@Entity(tableName = "events")
data class AnalyticsEventEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "session_id") val sessionId: String,
    @ColumnInfo(name = "event_name") val eventName: String,
    @ColumnInfo(name = "properties") val properties: String, // JSON string
    @ColumnInfo(name = "timestamp") val timestamp: Long = Instant.now().toEpochMilli()


) {
    fun toJson(): String {
        return Gson().toJson(this) // Serialize eventEntity to JSON
    }
}

@Entity(tableName = "sessions")
data class AnalyticsSessionEntity(
    @PrimaryKey val session_id: String,
    val startTime: Long = Instant.now().toEpochMilli(),
    val endTime: Long? = null
)

data class SessionWithEvents(
    @Embedded val session: AnalyticsSessionEntity,
    @Relation(
        parentColumn = "session_id",
        entityColumn = "session_id"
    )
    val events: List<AnalyticsEventEntity>
)