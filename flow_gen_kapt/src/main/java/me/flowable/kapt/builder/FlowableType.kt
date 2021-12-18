package me.flowable.kapt.builder

import me.flowable.domain.annotation.OnBufferOverflow

sealed interface FlowableType {

    object Default : FlowableType

    object State : FlowableType

    class Shared(
        val replay: Int,
        val extraBufferCapacity: Int,
        val onBufferOverflow: OnBufferOverflow
    ) : FlowableType

    object Transient : FlowableType
}
