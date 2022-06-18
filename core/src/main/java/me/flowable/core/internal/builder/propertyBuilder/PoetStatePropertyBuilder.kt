package me.flowable.core.internal.builder.propertyBuilder

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import me.flowable.core.internal.builder.FlowableType
import me.flowable.core.internal.builder.model.PoetPropertiesHolder
import me.flowable.core.internal.builder.model.PropertyScheme
import me.flowable.core.internal.ext.uppercaseFirstChar
import com.squareup.kotlinpoet.TypeName as PoetTypeName

object PoetStatePropertyBuilder : PoetPropertyBuilder<FlowableType.State>() {

    private val STATE_FLOW_TYPE = StateFlow::class
    private val STATE_FLOW_PACKAGE = STATE_FLOW_TYPE.java.`package`.name
    private val STATE_FLOW_NAME = STATE_FLOW_TYPE.simpleName!!

    private val MUTABLE_STATE_FLOW_TYPE = MutableStateFlow::class
    private val MUTABLE_STATE_FLOW_PACKAGE = MUTABLE_STATE_FLOW_TYPE.java.`package`.name
    private val MUTABLE_STATE_FLOW_NAME = MUTABLE_STATE_FLOW_TYPE.simpleName!!

    override fun build(
        propertyScheme: PropertyScheme<FlowableType.State>,
        implClassName: String
    ): PoetPropertiesHolder {
        val poetTypeName = propertyScheme.typeName.accept(typeNameVisitor)
        val initialParamName = makeInitialParamName(propertyScheme.name)
        val propName = makePropName(initialParamName)

        val baseParameterBuilder = makeBaseParameterBuilder(propName, poetTypeName)
        val implParameter = baseParameterBuilder.makeImplParameter()

        val basePropertyBuilder = makeBasePropertyBuilder(propName, poetTypeName)
        val interfaceProperty = basePropertyBuilder.makeInterfaceProperty()
        val implProperty = makeImplProperty(propName, poetTypeName, implParameter)
        val immutableProperty = implProperty.makeImmutableProperty(implClassName.name)

        return PoetPropertiesHolder(
            interfaceProperty = interfaceProperty,
            implProperty = implProperty,
            implParameter = implParameter,
            immutableProperty = immutableProperty
        )
    }

    private fun makeBaseParameterBuilder(name: String, poetTypeName: PoetTypeName) =
        ParameterSpec.builder(name, poetTypeName)

    private fun ParameterSpec.Builder.makeImplParameter() = build()

    private fun makeBasePropertyBuilder(name: String, poetTypeName: PoetTypeName) =
        PropertySpec.builder(name, makeBasePropTypeName(poetTypeName))

    private fun PropertySpec.Builder.makeInterfaceProperty() = build()

    private fun makeImplProperty(
        name: String,
        poetTypeName: PoetTypeName,
        parameterSpec: ParameterSpec
    ) = PropertySpec.builder(name, makeImplPropTypeName(poetTypeName))
        .initializeStateFlowPropImpl(parameterSpec.name)
        .build()

    private fun PropertySpec.makeImmutableProperty(
        implClassName: String
    ) = toBuilder().getter(
        FunSpec.getterBuilder()
            .addCode("return this@%L.%L", implClassName, name)
            .build()
    ).build()

    private fun makeInitialParamName(initialPropName: String) =
        "initial${initialPropName.uppercaseFirstChar()}State"

    private fun PropertySpec.Builder.initializeStateFlowPropImpl(initialParamName: String) =
        initializer("%T(%N)", MUTABLE_STATE_FLOW_TYPE, initialParamName)

    private fun makePropName(initialPropName: String) = "${initialPropName}StateFlow"

    private fun makeBasePropTypeName(initialPoetTypeName: PoetTypeName): PoetTypeName {
        val flowClassName = ClassName(
            STATE_FLOW_PACKAGE,
            STATE_FLOW_NAME
        )

        return flowClassName.parameterizedBy(initialPoetTypeName)
    }

    private fun makeImplPropTypeName(initialPoetTypeName: PoetTypeName): PoetTypeName {
        val flowClassName = ClassName(
            MUTABLE_STATE_FLOW_PACKAGE,
            MUTABLE_STATE_FLOW_NAME
        )

        return flowClassName.parameterizedBy(initialPoetTypeName)
    }
}
