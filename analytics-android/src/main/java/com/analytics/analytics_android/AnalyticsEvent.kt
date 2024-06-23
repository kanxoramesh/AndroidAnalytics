package com.analytics.analytics_android

import android.content.Context
import java.io.Serializable

/**
 * Handle events for Analytics.
 *
 */
open class AnalyticsEvent(
    val name: String,
    var attributes: MutableMap<String, Any>? = null) : Serializable {

    /**
     * Access the event name.
     *
     * @return the name of the custom event
     */
    fun name(): String {
        return name
    }

    /**
     * Adds an attribute to the event.
     *
     * @param attributeName the name of the attribute (should be unique)
     * @param value         the [Object] to associate with the name given
     * @return the [AnalyticsEvent] instance
     */
    fun putAttribute(attributeName: String, value: Any): AnalyticsEvent {
        if (attributes == null) {
            attributes = LinkedHashMap()
        }
        attributes!![attributeName] = value
        return this
    }


    /**
     * Access a single attribute of this event.
     *
     * @param name the name the of the attribute to retrieve
     * @return the value associated with the given attribute name.
     * Returns `null` if the attribute has not been set.
     */
    fun getAttribute(name: String): Any? {
        return if (attributes == null) null else attributes!![name]
    }

    /**
     * Access the attributes of this event.
     * @return A non-empty map of attributes set on this event.
     * Returns `null` if no attributes have been added to the event.
     */
    @JvmName("getEventAttributes")
    fun getAttributes(): Map<String, Any>? = attributes


    /**
     * Sends the event .
     */
    fun send(context: Context): AnalyticsEvent {
        AndroidAnalyticsImpl.get(context).logEvent(this)
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AnalyticsEvent) return false

        if (name != other.name) return false
        if (attributes != other.attributes) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + (attributes?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "AnalyticsEvent(name='$name', attributes=$attributes)"
    }

    companion object {
        @JvmStatic
        private val serialVersionUID: Long = 1
    }
}