package me.flowable.kapt.builder.model

import me.flowable.kapt.builder.FlowableType
import me.flowable.kapt.traverse.typeName.TypeName

data class PropertyScheme<T : FlowableType>(
    val typeName: TypeName,
    val name: String,
    val type: T
)
