package me.flowable.core.internal.builder.propertyBuilder

import me.flowable.core.internal.builder.FlowableType
import me.flowable.core.internal.builder.model.PoetPropertiesHolder
import me.flowable.core.internal.builder.model.PropertyScheme
import me.flowable.core.internal.traverse.typeNameVisitor.TypeNameToKotlinPoetTypeNameVisitor

sealed class PoetPropertyBuilder<T : FlowableType> {

    protected val typeNameVisitor = TypeNameToKotlinPoetTypeNameVisitor()

    abstract fun build(
        propertyScheme: PropertyScheme<T>,
        implClassName: String
    ): PoetPropertiesHolder
}
