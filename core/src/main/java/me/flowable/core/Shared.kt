package me.flowable.core

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
annotation class Shared(
    val replay: Int = 1,
    val extraBufferCapacity: Int = 0,
    val onBufferOverflow: Int = OnBufferOverflowDef.SUSPEND
)

// Kotlin KSP doesn't support enum classes (at least, yet),
// so we're supposed to use integer constants instead
// TODO - wait for a KSP's update which fixes the issue
object OnBufferOverflowDef {
    const val SUSPEND = 0
    const val DROP_OLDEST = 1
    const val DROP_LATEST = 2
}
