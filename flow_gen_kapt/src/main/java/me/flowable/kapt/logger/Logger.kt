package me.flowable.kapt.logger

interface Logger {
    fun logInfo(message: String)

    fun logError(message: String)
}
