package me.flowable.core.internal.builder.model

import me.flowable.core.internal.builder.FlowableType
import me.flowable.core.internal.traverse.typeName.TypeName

data class PropertyScheme<T : FlowableType>(
    val typeName: TypeName,
    val name: String,
    val type: T
)
