package me.flowable.kapt.builder.propertyBuilder

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import kotlinx.coroutines.flow.MutableStateFlow
import me.flowable.kapt.builder.FlowableType
import me.flowable.kapt.builder.model.PropertyScheme
import me.flowable.kapt.builder.model.PoetTypeScheme
import me.flowable.kapt.ext.uppercaseFirstChar
import com.squareup.kotlinpoet.TypeName as PoetTypeName

object PoetStatePropertyBuilder : PoetPropertyBuilder<FlowableType.State>() {

    private val MUTABLE_STATE_FLOW_TYPE = MutableStateFlow::class
    private val STATE_FLOW_PACKAGE = MUTABLE_STATE_FLOW_TYPE.java.packageName
    private val MUTABLE_STATE_FLOW_NAME = MUTABLE_STATE_FLOW_TYPE.simpleName!!

    override fun build(scheme: PropertyScheme<FlowableType.State>): PoetTypeScheme {
        val poetTypeName = scheme.typeName.accept(typeNameVisitor)
        val initialParamName = makeInitialParamName(scheme.name)

        val parameter = ParameterSpec.builder(initialParamName, poetTypeName).build()

        val property = PropertySpec.builder(makePropName(scheme.name), makePropTypeName(poetTypeName))
            .initializeStateFlowProp(initialParamName)
            .build()

        return PoetTypeScheme.pair(parameter, property)
    }

    private fun makeInitialParamName(initialPropName: String) =
        "initial${initialPropName.uppercaseFirstChar()}State"

    private fun makePropName(initialPropName: String) = "${initialPropName}StateFlow"

    private fun makePropTypeName(initialPoetTypeName: PoetTypeName): PoetTypeName {
        val flowClassName = ClassName(
            STATE_FLOW_PACKAGE,
            MUTABLE_STATE_FLOW_NAME
        )

        return flowClassName.parameterizedBy(initialPoetTypeName)
    }

    private fun PropertySpec.Builder.initializeStateFlowProp(initialParamName: String) =
        initializer("%T(%N)", MUTABLE_STATE_FLOW_TYPE, initialParamName)
}
