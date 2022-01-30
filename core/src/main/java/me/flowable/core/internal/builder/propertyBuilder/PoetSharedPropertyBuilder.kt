package me.flowable.core.internal.builder.propertyBuilder

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import me.flowable.core.internal.builder.FlowableType
import me.flowable.core.internal.builder.OnBufferOverflow
import me.flowable.core.internal.builder.model.PropertyScheme
import me.flowable.core.internal.builder.model.PoetTypeScheme
import me.flowable.core.internal.traverse.typeName.TypeName
import com.squareup.kotlinpoet.TypeName as PoetTypeName

object PoetSharedPropertyBuilder : PoetPropertyBuilder<FlowableType.Shared>() {

    private val MUTABLE_SHARED_FLOW_TYPE = MutableSharedFlow::class
    private val SHARED_FLOW_PACKAGE = MUTABLE_SHARED_FLOW_TYPE.java.`package`.name
    private val MUTABLE_SHARED_FLOW_NAME = MUTABLE_SHARED_FLOW_TYPE.simpleName!!
    private val MUTABLE_SHARED_FLOW_BUFFER_OVERFLOW_TYPE = BufferOverflow::class

    override fun build(scheme: PropertyScheme<FlowableType.Shared>): PoetTypeScheme {
        val property = PropertySpec.builder(makePropName(scheme.name), makeTypeName(scheme.typeName))
            .initializeSharedFlowProp(scheme.type)
            .build()

        return PoetTypeScheme.single(property)
    }

    private fun makePropName(initialPropName: String) = "${initialPropName}SharedFlow"

    private fun makeTypeName(initialTypeName: TypeName): PoetTypeName {
        val className = initialTypeName.accept(typeNameVisitor)
        val flowClassName = ClassName(
            SHARED_FLOW_PACKAGE,
            MUTABLE_SHARED_FLOW_NAME
        )

        return flowClassName.parameterizedBy(className)
    }

    private fun PropertySpec.Builder.initializeSharedFlowProp(type: FlowableType.Shared) =
        initializer(
            "%T(%L, %L, %T.%L)",
            MUTABLE_SHARED_FLOW_TYPE,
            type.replay,
            type.extraBufferCapacity,
            MUTABLE_SHARED_FLOW_BUFFER_OVERFLOW_TYPE,
            type.onBufferOverflow.mapToFlowOnBufferOverflow()
        )

    private fun OnBufferOverflow.mapToFlowOnBufferOverflow() = when (this) {
        OnBufferOverflow.SUSPEND -> BufferOverflow.SUSPEND
        OnBufferOverflow.DROP_OLDEST -> BufferOverflow.DROP_OLDEST
        OnBufferOverflow.DROP_LATEST -> BufferOverflow.DROP_LATEST
    }
}
