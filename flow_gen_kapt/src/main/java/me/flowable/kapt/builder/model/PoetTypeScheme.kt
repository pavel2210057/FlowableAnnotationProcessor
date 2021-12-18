package me.flowable.kapt.builder.model

import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec

data class PoetTypeScheme(
    val parameters: MutableList<ParameterSpec> = mutableListOf(),
    val properties: MutableList<PropertySpec> = mutableListOf()
) {
    companion object {

        fun single(parameterSpec: ParameterSpec) =
            PoetTypeScheme(mutableListOf(parameterSpec), mutableListOf())

        fun single(propertySpec: PropertySpec) =
            PoetTypeScheme(mutableListOf(), mutableListOf(propertySpec))

        fun pair(parameterSpec: ParameterSpec, propertySpec: PropertySpec) =
            PoetTypeScheme(mutableListOf(parameterSpec), mutableListOf(propertySpec))
    }
}
