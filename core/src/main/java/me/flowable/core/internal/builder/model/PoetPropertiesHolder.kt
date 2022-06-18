package me.flowable.core.internal.builder.model

import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec

data class PoetPropertiesHolder(
    val interfaceProperty: PropertySpec,
    val implProperty: PropertySpec,
    val implParameter: ParameterSpec?,
    val immutableProperty: PropertySpec
)
