package me.flowable.kapt.builder.propertyBuilder

import me.flowable.domain.annotation.OnBufferOverflow
import me.flowable.kapt.builder.FlowableType
import me.flowable.kapt.builder.model.PropertyScheme

object PoetDefaultPropertyBuilder : PoetPropertyBuilder<FlowableType.Default>() {

    private const val DEFAULT_FLOW_REPLAY = 1
    private const val DEFAULT_FLOW_EXTRA_BUFFER_CAPACITY = 0
    private val DEFAULT_BUFFER_OVERFLOW_STRATEGY = OnBufferOverflow.SUSPEND

    override fun build(scheme: PropertyScheme<FlowableType.Default>) =
        PoetSharedPropertyBuilder.build(
            PropertyScheme(
                scheme.typeName,
                scheme.name,
                FlowableType.Shared(
                    DEFAULT_FLOW_REPLAY,
                    DEFAULT_FLOW_EXTRA_BUFFER_CAPACITY,
                    DEFAULT_BUFFER_OVERFLOW_STRATEGY
                )
            )
        )
}