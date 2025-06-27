package com.pds.localpos.common.util

object CorrelationIdHolder {
    private val correlationId = ThreadLocal<String>()

    fun set(id: String) {
        correlationId.set(id)
    }

    fun get(): String? {
        return correlationId.get()
    }

    fun clear() {
        correlationId.remove()
    }
}
