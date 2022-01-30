package me.flowable.core.internal.logger

interface Logger {
    fun logInfo(message: String)

    fun logError(message: String)
}
