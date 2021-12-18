package me.flowable.kapt.builder.propertyBuilder

import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import me.flowable.kapt.builder.FlowableType
import me.flowable.kapt.builder.model.PropertyScheme
import me.flowable.kapt.builder.model.PoetTypeScheme

object PoetTransientPropertyBuilder : PoetPropertyBuilder<FlowableType.Transient>() {

    override fun build(scheme: PropertyScheme<FlowableType.Transient>): PoetTypeScheme {
        val poetTypeName = scheme.typeName.accept(typeNameVisitor)
        val parameter = ParameterSpec.builder(scheme.name, poetTypeName).build()
        val property = PropertySpec.builder(scheme.name, poetTypeName)
            .initializer(scheme.name)
            .build()

        return PoetTypeScheme.pair(parameter, property)
    }
}
