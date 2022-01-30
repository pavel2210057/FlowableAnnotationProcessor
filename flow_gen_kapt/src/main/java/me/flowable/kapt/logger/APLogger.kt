package me.flowable.kapt.logger

import me.flowable.core.internal.logger.Logger
import java.lang.ref.WeakReference
import javax.annotation.processing.Messager
import javax.tools.Diagnostic

object GlobalLogger : Logger {
    private var logger: WeakReference<Logger>? = null

    fun initLogger(messager: Messager) {
        logger = WeakReference(
            APLogger(messager)
        )
    }

    override fun logInfo(message: String) {
        logger!!.get()?.logInfo(message)
    }

    override fun logError(message: String) {
        logger!!.get()?.logError(message)
    }
}

class APLogger(
    private val messager: Messager
) : Logger {

    override fun logInfo(message: String) {
        messager.printMessage(Diagnostic.Kind.NOTE, message)
    }

    override fun logError(message: String) {
        messager.printMessage(Diagnostic.Kind.ERROR, message)
    }
}
