package me.flowable.core.internal.builder

import com.squareup.kotlinpoet.*
import me.flowable.core.Shared
import me.flowable.core.State
import me.flowable.core.Skip
import me.flowable.core.internal.builder.model.PoetPropertiesHolder
import me.flowable.core.internal.builder.model.PoetTypeScheme
import me.flowable.core.internal.builder.model.PropertyScheme
import me.flowable.core.internal.builder.propertyBuilder.PoetDefaultPropertyBuilder
import me.flowable.core.internal.builder.propertyBuilder.PoetSharedPropertyBuilder
import me.flowable.core.internal.builder.propertyBuilder.PoetStatePropertyBuilder
import me.flowable.core.internal.builder.propertyBuilder.PoetTransientPropertyBuilder
import me.flowable.core.internal.traverse.ClassScheme
import me.flowable.core.internal.traverse.Variable

class FlowableSourceBuilder {

    fun buildByClassScheme(classScheme: ClassScheme): FileSpec = buildFlowableClass(classScheme)

    private fun buildFlowableClass(classScheme: ClassScheme): FileSpec {
        val packageName = classScheme.qualifiedName.substringBeforeLast('.')
        val baseClassName = classScheme.qualifiedName.substringAfterLast('.')

        val interfaceName = makeFlowableInterfaceName(baseClassName)
        val implName = makeFlowableImplName(interfaceName)

        val fileSpecBuilder = FileSpec.builder(packageName, interfaceName)

        val typePoetScheme = classScheme.variables.fold(PoetTypeScheme()) { acc, variable ->
            val propHolder = buildPoetPropertiesHolder(variable, implName)

            PoetTypeScheme(
                interfaceProperties = acc.interfaceProperties + propHolder.interfaceProperty,
                implProperties = acc.implProperties + propHolder.implProperty,
                implParameters = propHolder.implParameter?.let { acc.implParameters + it }
                    ?: acc.implParameters,
                immutableProperties = acc.immutableProperties + propHolder.immutableProperty
            )
        }

        val flowableInterfaceSpec = makeFlowableInterface(packageName, interfaceName,
            typePoetScheme.interfaceProperties, implName, typePoetScheme.implParameters)
        val flowableImplSpec = makeFlowableImpl(implName, packageName, interfaceName,
            typePoetScheme)

        return fileSpecBuilder
            .addType(flowableInterfaceSpec)
            .addType(flowableImplSpec)
            .build()
    }

    private fun makeFlowableInterfaceName(initialClassName: String) = "${initialClassName}Flowable"

    private fun makeFlowableImplName(interfaceName: String) = "${interfaceName}Impl"

    private fun buildPoetPropertiesHolder(
        variable: Variable,
        implClassName: String
    ): PoetPropertiesHolder {
        val flowableType = mapFlowableAnnotationToFlowableType(variable.flowableAnnotation)
        val scheme = PropertyScheme(
            variable.typeName,
            variable.name,
            flowableType
        )

        return buildPoetPropertiesHolder(scheme, implClassName)
    }

    private fun mapFlowableAnnotationToFlowableType(flowableAnnotation: Annotation?): FlowableType {
        if (flowableAnnotation == null)
            return FlowableType.Default

        return when (flowableAnnotation) {
            is State -> FlowableType.State
            is Shared -> FlowableType.Shared(
                replay = flowableAnnotation.replay,
                extraBufferCapacity = flowableAnnotation.extraBufferCapacity,
                onBufferOverflow = flowableAnnotation.onBufferOverflow.onBufferOverflowValue
            )
            is Skip -> FlowableType.Transient
            else -> throw error("Unexpected behavior!")
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun<T : FlowableType> buildPoetPropertiesHolder(
        propertyScheme: PropertyScheme<T>,
        implClassName: String
    ) = when (propertyScheme.type) {
        FlowableType.Default -> PoetDefaultPropertyBuilder
            .build(propertyScheme as PropertyScheme<FlowableType.Default>, implClassName)

        FlowableType.State -> PoetStatePropertyBuilder
            .build(propertyScheme as PropertyScheme<FlowableType.State>, implClassName)

        is FlowableType.Shared -> PoetSharedPropertyBuilder
            .build(propertyScheme as PropertyScheme<FlowableType.Shared>, implClassName)

        FlowableType.Transient -> PoetTransientPropertyBuilder
            .build(propertyScheme as PropertyScheme<FlowableType.Transient>, implClassName)

        else -> throw error("Unexpected behavior!")
    }

    private fun makeFlowableInterface(
        packageName: String,
        name: String,
        interfaceProperties: List<PropertySpec>,
        implClassName: String,
        implParameters: List<ParameterSpec>
    ) =
        TypeSpec.interfaceBuilder(name)
            .addModifiers(KModifier.PUBLIC)
            .addProperties(interfaceProperties)
            .addType(
                makeFlowableInterfaceCompanion(
                    ClassName(packageName, name),
                    implClassName,
                    implParameters
                )
            )
            .build()

    private fun makeFlowableInterfaceCompanion(
        interfaceTypeName: TypeName,
        implClassName: String,
        implClassParameters: List<ParameterSpec>
    ) =
        TypeSpec.companionObjectBuilder()
            .addFunction(
                FunSpec.builder(INVOKE_FUN_NAME)
                    .addModifiers(KModifier.PUBLIC, KModifier.OPERATOR)
                    .addParameters(implClassParameters)
                    .addCode("return %L(", implClassName)
                    .apply {
                        implClassParameters.forEach { addCode("%N, ", it) }
                    }
                    .addCode(")")
                    .build()
            )
            .build()

    private fun makeFlowableImpl(
        name: String,
        interfacePackage: String,
        interfaceName: String,
        poetTypeScheme: PoetTypeScheme
    ): TypeSpec {
        val interfaceTypeName = ClassName(interfacePackage, interfaceName)

        return TypeSpec.classBuilder(name)
            .addModifiers(KModifier.PUBLIC)
            .addSuperinterface(interfaceTypeName)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameters(poetTypeScheme.implParameters)
                    .build()
            )
            .addProperties(poetTypeScheme.implProperties)
            .addFunction(
                FunSpec.builder(IMMUTABLE_FUN_NAME)
                    .addModifiers(KModifier.PUBLIC)
                    .addStatement("return %L", makeFlowableImmutable(
                        interfaceTypeName,
                        poetTypeScheme.immutableProperties
                    ))
                    .build()
            )
            .build()
    }

    private fun makeFlowableImmutable(
        interfaceTypeName: TypeName,
        immutableProperties: List<PropertySpec>
    ) =
        TypeSpec.anonymousClassBuilder()
            .addSuperinterface(interfaceTypeName)
            .addProperties(immutableProperties)
            .build()

    companion object {
        private const val IMMUTABLE_FUN_NAME = "immutable"
        private const val INVOKE_FUN_NAME = "invoke"
    }
}
