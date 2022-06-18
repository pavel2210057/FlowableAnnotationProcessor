package me.flowable.core.internal.builder.propertyBuilder

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import me.flowable.core.internal.builder.FlowableType
import me.flowable.core.internal.builder.OnBufferOverflow
import me.flowable.core.internal.builder.model.PoetPropertiesHolder
import me.flowable.core.internal.builder.model.PropertyScheme
import com.squareup.kotlinpoet.TypeName as PoetTypeName

object PoetSharedPropertyBuilder : PoetPropertyBuilder<FlowableType.Shared>() {

    private val SHARED_FLOW_TYPE = SharedFlow::class
    private val SHARED_FLOW_PACKAGE = SHARED_FLOW_TYPE.java.`package`.name
    private val SHARED_FLOW_NAME = SHARED_FLOW_TYPE.simpleName!!

    private val MUTABLE_SHARED_FLOW_TYPE = MutableSharedFlow::class
    private val MUTABLE_SHARED_FLOW_PACKAGE = MUTABLE_SHARED_FLOW_TYPE.java.`package`.name
    private val MUTABLE_SHARED_FLOW_NAME = MUTABLE_SHARED_FLOW_TYPE.simpleName!!
    private val MUTABLE_SHARED_FLOW_BUFFER_OVERFLOW_TYPE = BufferOverflow::class

    override fun build(
        propertyScheme: PropertyScheme<FlowableType.Shared>,
        implClassName: String
    ): PoetPropertiesHolder {
        val poetTypeName = propertyScheme.typeName.accept(typeNameVisitor)
        val propName = makePropName(propertyScheme.name)

        val basePropertyBuilder = makeBasePropertyBuilder(propName, poetTypeName)
        val interfaceProperty = basePropertyBuilder.makeInterfaceProperty()
        val implProperty = makeImplProperty(propName, poetTypeName, propertyScheme.type)
        val immutableProperty = implProperty.makeImmutableProperty(implClassName.name)

        return PoetPropertiesHolder(
            interfaceProperty = interfaceProperty,
            implProperty = implProperty,
            implParameter = null,
            immutableProperty = immutableProperty
        )
    }

    private fun makeBasePropertyBuilder(name: String, poetTypeName: PoetTypeName) =
        PropertySpec.builder(name, makeBaseTypeName(poetTypeName))

    private fun PropertySpec.Builder.makeInterfaceProperty() = build()

    private fun makeImplProperty(
        name: String,
        poetTypeName: PoetTypeName,
        type: FlowableType.Shared
    ) =
        PropertySpec.builder(name, makeTypeNameImpl(poetTypeName))
            .initializeSharedFlowPropImpl(type)
            .build()

    private fun PropertySpec.makeImmutableProperty(
        implClassName: String
    ) = toBuilder().getter(
        FunSpec.getterBuilder()
            .addCode("return this@%L.%L", implClassName, name)
            .build()
    ).build()

    private fun makeBaseTypeName(initialTypeName: PoetTypeName): PoetTypeName {
        val flowClassName = ClassName(
            SHARED_FLOW_PACKAGE,
            SHARED_FLOW_NAME
        )

        return flowClassName.parameterizedBy(initialTypeName)
    }

    private fun makeTypeNameImpl(initialTypeName: PoetTypeName): PoetTypeName {
        val flowClassName = ClassName(
            MUTABLE_SHARED_FLOW_PACKAGE,
            MUTABLE_SHARED_FLOW_NAME
        )

        return flowClassName.parameterizedBy(initialTypeName)
    }

    private fun PropertySpec.Builder.initializeSharedFlowPropImpl(type: FlowableType.Shared) =
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

    private fun makePropName(initialPropName: String) = "${initialPropName}SharedFlow"
}
