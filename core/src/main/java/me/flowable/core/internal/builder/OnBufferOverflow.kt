package me.flowable.core.internal.builder

import me.flowable.core.OnBufferOverflowDef

enum class OnBufferOverflow {
    SUSPEND,
    DROP_OLDEST,
    DROP_LATEST
}

// a hack for KSP's impossibility of
// handling enum classes
// TODO - wait for a KSP's update which fixes the issue
val Int.onBufferOverflowValue: OnBufferOverflow
get() = when(this) {
    OnBufferOverflowDef.SUSPEND -> OnBufferOverflow.SUSPEND
    OnBufferOverflowDef.DROP_OLDEST -> OnBufferOverflow.DROP_OLDEST
    OnBufferOverflowDef.DROP_LATEST -> OnBufferOverflow.DROP_LATEST
    else -> throw error("Illegal state!")
}
