package me.flowable.core.internal.builder.propertyBuilder

import com.squareup.kotlinpoet.*
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
        val immutableProperty = interfaceProperty.makeImmutableProperty(implClassName)

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

    private fun PropertySpec.makeImplProperty() = toBuilder()
        .addModifiers(KModifier.OVERRIDE, KModifier.PUBLIC)
        .initializer(name)
        .build()

    private fun PropertySpec.makeImmutableProperty(
        implClassName: String
    ) = toBuilder()
        .addModifiers(KModifier.OVERRIDE)
        .getter(
            FunSpec.getterBuilder()
                .addCode("return this@%L.%L", implClassName, name)
                .build()
        ).build()
}
