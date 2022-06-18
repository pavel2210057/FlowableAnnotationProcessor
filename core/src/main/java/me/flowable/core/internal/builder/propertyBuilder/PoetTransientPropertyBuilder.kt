package me.flowable.core.internal.builder.propertyBuilder

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import me.flowable.core.internal.builder.FlowableType
import me.flowable.core.internal.builder.model.PoetPropertiesHolder
import me.flowable.core.internal.builder.model.PropertyScheme

object PoetTransientPropertyBuilder : PoetPropertyBuilder<FlowableType.Transient>() {

    override fun build(
        propertyScheme: PropertyScheme<FlowableType.Transient>,
        implClassName: String
    ): PoetPropertiesHolder {
        val poetTypeName = propertyScheme.typeName.accept(typeNameVisitor)

        val baseProperty = makeBasePropertyBuilder(propertyScheme.name, poetTypeName)
        val interfaceProperty = baseProperty.makeInterfaceProperty()
        val implProperty = interfaceProperty.makeImplProperty()
        val immutableProperty = implProperty.makeImmutableProperty(implClassName.name)

        val baseParameter = makeBaseParameterBuilder(propertyScheme.name, poetTypeName)
        val implParameter = baseParameter.makeImplParameter()

        return PoetPropertiesHolder(
            interfaceProperty = interfaceProperty,
            implProperty = implProperty,
            implParameter = implParameter,
            immutableProperty = immutableProperty
        )
    }

    private fun makeBaseParameterBuilder(name: String, poetTypeName: TypeName) =
        ParameterSpec.builder(name, poetTypeName)

    private fun ParameterSpec.Builder.makeImplParameter() = build()

    private fun makeBasePropertyBuilder(name: String, poetTypeName: TypeName) =
        PropertySpec.builder(name, poetTypeName)

    private fun PropertySpec.Builder.makeInterfaceProperty() = build()

    private fun PropertySpec.makeImplProperty() = toBuilder().initializer(name).build()

    private fun PropertySpec.makeImmutableProperty(
        implClassName: String
    ) = toBuilder().getter(
        FunSpec.getterBuilder()
            .addCode("return this@%T.%L", implClassName, name)
            .build()
    ).build()
}
