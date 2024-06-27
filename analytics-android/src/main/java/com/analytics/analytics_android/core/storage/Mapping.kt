package com.analytics.analytics_android.core.storage

import com.analytics.analytics_android.AnalyticsEvent
import com.analytics.analytics_android.Session
import com.analytics.analytics_android.core.session.AnalyticsSession
import com.google.gson.Gson
import java.time.Instant
import java.util.UUID

object Mapping {
    private val gson = Gson()

    fun sessionToEntity(session: Session): AnalyticsSessionEntity {
        return AnalyticsSessionEntity(
            sessionId = session.sessionId ?: UUID.randomUUID().toString(),
            startTime = session.startTime ?: Instant.now().toEpochMilli(),
            endTime = session.endTime
        )
    }

//    fun entityToSession(entity: AnalyticsSessionEntity, events: Collection<AnalyticsEventEntity>): Session {
//        return AnalyticsSession(
//            sessionId = entity.sessionId,
//            startTime = Instant.ofEpochMilli(entity.startTime),
//            endTime = entity.endTime?.let { Instant.ofEpochMilli(it) }
//        ).apply {
//            this.events.addAll(events)
//        }
//    }
//
//    fun entityToEvent(entity: AnalyticsEventEntity): AnalyticsEvent {
//        return AnalyticsEvent(entity.eventName,entity.sessionId)
//    }


    fun eventToJson(event: AnalyticsEventEntity): String {
        return gson.toJson(event.properties)
    }

    fun jsonToEvent(sessionId: String, eventName: String, json: String, timestamp: Long): AnalyticsEventEntity {
        val properties: Map<*, *>? = gson.fromJson(json, Map::class.java)
        return AnalyticsEventEntity(
            sessionId = sessionId,
            eventName = eventName,
            properties = properties.toString(),
            timestamp = timestamp
        )
    }
}
