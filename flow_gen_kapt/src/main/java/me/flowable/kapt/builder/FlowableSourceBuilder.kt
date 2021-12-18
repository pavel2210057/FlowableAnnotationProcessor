package me.flowable.kapt.builder

import com.squareup.kotlinpoet.*
import me.flowable.domain.annotation.Shared
import me.flowable.domain.annotation.State
import me.flowable.domain.annotation.Skip
import me.flowable.kapt.builder.model.PoetTypeScheme
import me.flowable.kapt.builder.model.PropertyScheme
import me.flowable.kapt.builder.propertyBuilder.PoetDefaultPropertyBuilder
import me.flowable.kapt.builder.propertyBuilder.PoetSharedPropertyBuilder
import me.flowable.kapt.builder.propertyBuilder.PoetStatePropertyBuilder
import me.flowable.kapt.builder.propertyBuilder.PoetTransientPropertyBuilder
import me.flowable.kapt.traverse.ClassScheme
import me.flowable.kapt.traverse.Variable

class FlowableSourceBuilder {

    fun buildByClassScheme(classScheme: ClassScheme): FileSpec = buildFlowableClass(classScheme)

    private fun buildFlowableClass(classScheme: ClassScheme): FileSpec {
        val packageName = classScheme.qualifiedName.substringBeforeLast('.')
        val className = makeFlowableClassName(
            classScheme.qualifiedName.substringAfterLast('.')
        )

        val fileSpecBuilder = FileSpec.builder(packageName, className)

        val typeSpecBuilder = TypeSpec.classBuilder(className)
            .addModifiers(KModifier.PUBLIC)

        val typePoetScheme = PoetTypeScheme()
        classScheme.variables.forEach { variable ->
            val propPoetScheme = buildPropPoetScheme(variable)

            typePoetScheme.parameters += propPoetScheme.parameters
            typePoetScheme.properties += propPoetScheme.properties
        }

        val typeSpec = buildPoetType(typeSpecBuilder, typePoetScheme)
        return fileSpecBuilder
            .addType(typeSpec)
            .build()
    }

    private fun makeFlowableClassName(initialClassName: String) = "${initialClassName}Flowable"

    private fun buildPropPoetScheme(
        variable: Variable
    ): PoetTypeScheme {
        val flowableType = mapFlowableAnnotationToFlowableType(variable.flowableAnnotation)
        val scheme = PropertyScheme(
            variable.typeName,
            variable.name,
            flowableType
        )

        return buildPropPoetScheme(scheme)
    }

    private fun mapFlowableAnnotationToFlowableType(flowableAnnotation: Annotation?): FlowableType {
        if (flowableAnnotation == null)
            return FlowableType.Default

        return when (flowableAnnotation) {
            is State -> FlowableType.State
            is Shared -> FlowableType.Shared(
                replay = flowableAnnotation.replay,
                extraBufferCapacity = flowableAnnotation.extraBufferCapacity,
                onBufferOverflow = flowableAnnotation.onBufferOverflow
            )
            is Skip -> FlowableType.Transient
            else -> throw error("Unexpected behavior!")
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun<T : FlowableType> buildPropPoetScheme(
        propertyScheme: PropertyScheme<T>
    ) = when (propertyScheme.type) {
        FlowableType.Default -> PoetDefaultPropertyBuilder.build(propertyScheme as PropertyScheme<FlowableType.Default>)
        FlowableType.State -> PoetStatePropertyBuilder.build(propertyScheme as PropertyScheme<FlowableType.State>)
        is FlowableType.Shared -> PoetSharedPropertyBuilder.build(propertyScheme as PropertyScheme<FlowableType.Shared>)
        FlowableType.Transient -> PoetTransientPropertyBuilder.build(propertyScheme as PropertyScheme<FlowableType.Transient>)
        else -> throw error("Unexpected behavior!")
    }

    private fun buildPoetType(typeSpecBuilder: TypeSpec.Builder, poetTypeScheme: PoetTypeScheme) =
        typeSpecBuilder.primaryConstructor(
            FunSpec.constructorBuilder()
                .addParameters(poetTypeScheme.parameters)
                .build()
        )
            .addProperties(poetTypeScheme.properties)
            .build()
}
