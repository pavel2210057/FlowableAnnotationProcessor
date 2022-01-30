package me.flowable.core.internal.builder.propertyBuilder

import me.flowable.core.internal.builder.FlowableType
import me.flowable.core.internal.builder.model.PropertyScheme
import me.flowable.core.internal.builder.model.PoetTypeScheme
import me.flowable.core.internal.traverse.typeNameVisitor.TypeNameToKotlinPoetTypeNameVisitor

sealed class PoetPropertyBuilder<T : FlowableType> {

    protected val typeNameVisitor = TypeNameToKotlinPoetTypeNameVisitor()

    abstract fun build(scheme: PropertyScheme<T>): PoetTypeScheme
}
