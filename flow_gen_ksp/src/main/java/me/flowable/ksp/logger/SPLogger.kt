package me.flowable.ksp.logger

import com.google.devtools.ksp.processing.KSPLogger
import me.flowable.core.internal.logger.Logger
import java.lang.ref.WeakReference

object GlobalLogger : Logger {

    private var logger: WeakReference<Logger>? = null

    fun init(logger: KSPLogger) {
        this.logger = WeakReference(
            SPLogger(logger)
        )
    }

    override fun logInfo(message: String) {
        logger!!.get()?.logInfo(message)
    }

    override fun logError(message: String) {
        logger!!.get()?.logError(message)
    }
}

class SPLogger(
    private val logger: KSPLogger
) : Logger {

    override fun logInfo(message: String) {
        logger.warn(message)
    }

    override fun logError(message: String) {
        logger.error(message)
    }
}
