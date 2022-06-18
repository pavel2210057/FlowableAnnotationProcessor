package me.flowable.core.internal.builder.propertyBuilder

import me.flowable.core.internal.builder.FlowableType
import me.flowable.core.internal.builder.OnBufferOverflow
import me.flowable.core.internal.builder.model.PropertyScheme

object PoetDefaultPropertyBuilder : PoetPropertyBuilder<FlowableType.Default>() {

    private const val DEFAULT_FLOW_REPLAY = 1
    private const val DEFAULT_FLOW_EXTRA_BUFFER_CAPACITY = 0
    private val DEFAULT_BUFFER_OVERFLOW_STRATEGY = OnBufferOverflow.SUSPEND

    override fun build(
        propertyScheme: PropertyScheme<FlowableType.Default>,
        implClassName: String
    ) =
        PoetSharedPropertyBuilder.build(
            PropertyScheme(
                propertyScheme.typeName,
                propertyScheme.name,
                FlowableType.Shared(
                    DEFAULT_FLOW_REPLAY,
                    DEFAULT_FLOW_EXTRA_BUFFER_CAPACITY,
                    DEFAULT_BUFFER_OVERFLOW_STRATEGY
                )
            ),
            implClassName
        )
}