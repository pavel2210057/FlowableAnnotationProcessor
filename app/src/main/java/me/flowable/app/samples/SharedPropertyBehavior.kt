package me.flowable.app.samples

import me.flowable.core.Flowable
import me.flowable.core.OnBufferOverflowDef
import me.flowable.core.Shared

/**
 * will be generated following class:
 *
 *  class SingleDefaultSharedPropertyFlowable {
 *      public val propSharedFlow: MutableSharedFlow<String> =
 *          MutableSharedFlow(1, 0, BufferOverflow.SUSPEND)
 *  }
 */
@Flowable
data class SingleDefaultSharedProperty(
    @Shared val prop: String
)

/**
 * will be generated following class:
 *
 *  class SingleAdvancedSharedPropertyFlowable {
 *      public val propSharedFlow: MutableSharedFlow<String> =
 *          MutableSharedFlow(5, 10, BufferOverflow.DROP_OLDEST)
 *  }
 */
@Flowable
data class SingleAdvancedSharedProperty(
    @Shared(
        replay = 5,
        extraBufferCapacity = 10,
        onBufferOverflow = OnBufferOverflowDef.DROP_OLDEST
    )
    val prop: String
)
