package me.flowable.core.internal.builder.model

import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec

data class PoetTypeScheme(
    val interfaceProperties: List<PropertySpec> = listOf(),
    val implProperties: List<PropertySpec> = listOf(),
    val implParameters: List<ParameterSpec> = listOf(),
    val immutableProperties: List<PropertySpec> = listOf()
)
