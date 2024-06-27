package com.example.myapplication

import android.content.Context
import android.content.SharedPreferences
import com.analytics.analytics_android.AnalyticsEvent
import com.analytics.analytics_android.Session
import com.analytics.analytics_android.Storage
import com.analytics.analytics_android.core.session.AnalyticsSession
import org.json.JSONObject

class AnalyticsStorage(context: Context): Storage {
    private val PREFS_NAME = "AnalyticsPrefs"
    private val SESSIONS_KEY = "sessions"

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

   override fun saveSession(session: Session) {
        val sessions = getSessions().toMutableList()
        sessions.add(session.getSessionData())

        sharedPreferences.edit().putString(SESSIONS_KEY, sessions.toString()).apply()
    }
    override fun UpdateSession(endTime: Long){

        sharedPreferences.edit().putString(SESSIONS_KEY, sessions.toString()).apply()
    }

    override fun getSessions(): List<Map<String, Any?>> {
        val sessionsJson = sharedPreferences.getString(SESSIONS_KEY, "[]")
        // Parse the JSON string back to a list of maps (use a JSON library like Gson or Moshi)
        return parseSessionsJson(sessionsJson ?: "[]")
    }

    override fun getSession(sessionId: String): AnalyticsSession? {
        TODO("Not yet implemented")
    }

    override fun saveEvent(event: AnalyticsEvent) {
        TODO("Not yet implemented")
    }

    override fun getEvents(sessionId: String): List<AnalyticsEvent> {
        TODO("Not yet implemented")
    }

    private fun parseSessionsJson(json: String): List<Map<String, Any>> {
        // Implement JSON parsing logic
        return listOf()
    }
}
