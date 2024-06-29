package com.analytics.analytics_android

/**
 * Controller for managing Sessions.
 */
interface SessionController  {


    /**
     * The session .
     */
    val currentSession: Session?


    /**
     * Pause the session tracking.
     * While the session is paused it can't expire or be updated.
     */
    fun pause()

    /**
     * Resume the session tracking.
     */
    fun resume()

    fun endSession()

    /**
     * Expire the current session even if the timeout is not triggered.
     */
    fun startSession(poolCount: Int, param: (Boolean, List<Session>?) -> Unit)
    fun addEvent(eventName: String, properties: Map<String, Any>)
    fun getSessions(limit: Int?): List<Session>
    fun removedSyncedData(ids:List<String>)
}
