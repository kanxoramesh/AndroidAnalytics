package com.analytics.analytics_android.core.storage

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [AnalyticsEventEntity::class, AnalyticsSessionEntity::class], version = 3)
abstract class AnalyticsDatabase : RoomDatabase() {
    abstract fun analyticsEventDao(): AnalyticsEventDao
    abstract fun analyticsSessionDao(): AnalyticsSessionDao
}
