package com.analytics.analytics_android.network

import com.analytics.analytics_android.Session
import com.analytics.analytics_android.core.storage.SessionWithEvents

/**
 * Request class that contains the payloads to send
 * to the collector.
 */
class Request(val payload: List<Session>)
