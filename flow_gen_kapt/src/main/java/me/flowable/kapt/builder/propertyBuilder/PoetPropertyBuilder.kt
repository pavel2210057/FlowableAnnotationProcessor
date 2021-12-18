package me.flowable.kapt.builder.propertyBuilder

import me.flowable.kapt.builder.FlowableType
import me.flowable.kapt.builder.model.PropertyScheme
import me.flowable.kapt.builder.model.PoetTypeScheme
import me.flowable.kapt.traverse.typeNameVisitor.TypeNameToKotlinPoetTypeNameVisitor

sealed class PoetPropertyBuilder<T : FlowableType> {

    protected val typeNameVisitor = TypeNameToKotlinPoetTypeNameVisitor()

    abstract fun build(scheme: PropertyScheme<T>): PoetTypeScheme
}
