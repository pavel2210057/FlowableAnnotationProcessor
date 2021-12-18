package me.flowable.domain.annotation

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
annotation class Shared(
    val replay: Int = 1,
    val extraBufferCapacity: Int = 0,
    val onBufferOverflow: OnBufferOverflow = OnBufferOverflow.SUSPEND
)

enum class OnBufferOverflow {
    SUSPEND,
    DROP_OLDEST,
    DROP_LATEST
}
