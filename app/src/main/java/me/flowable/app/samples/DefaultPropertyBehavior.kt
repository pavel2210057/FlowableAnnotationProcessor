package me.flowable.app.samples

import me.flowable.domain.annotation.Flowable

/**
 * will be generated following class:
 *
 *  class SingleDefaultPropertyFlowable {
 *      public val propSharedFlow: MutableSharedFlow<String>
 *          = MutableSharedFlow(1, 0, BufferOverflow.SUSPEND)
 *  }
 */
@Flowable
data class SingleDefaultProperty(
    val prop: String
)
