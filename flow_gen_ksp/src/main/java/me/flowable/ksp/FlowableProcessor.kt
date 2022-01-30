package me.flowable.ksp

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import me.flowable.core.Flowable
import me.flowable.core.Shared
import me.flowable.core.Skip
import me.flowable.core.State
import me.flowable.core.internal.builder.FlowableSourceBuilder
import me.flowable.core.internal.traverse.ClassScheme
import me.flowable.core.internal.traverse.Variable
import me.flowable.core.internal.traverse.typeName.ParameterizedTypeName
import me.flowable.core.internal.traverse.typeName.TypeName
import me.flowable.ksp.logger.GlobalLogger

@KspExperimental
class FlowableProcessor(
    private val codeGenerator: CodeGenerator
) : SymbolProcessor {

    private val sourceBuilder = FlowableSourceBuilder()

    private companion object {
        private val FLOWABLE_ANNOTATION_NAME = Flowable::class.qualifiedName!!
        val FLOWABLE_ANNOTATIONS = arrayOf(
            State::class,
            Shared::class,
            Skip::class
        )
    }

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val annotatedSymbols = resolver.getSymbolsWithAnnotation(FLOWABLE_ANNOTATION_NAME)
        processFlowable(annotatedSymbols)

        return emptyList()
    }

    private fun processFlowable(flowableClasses: Sequence<KSAnnotated>) {
        flowableClasses.forEach {
            if (it !is KSClassDeclaration) {
                GlobalLogger.logError(
                    "Flowable annotation is not applicable to non-class entities"
                )
                return@forEach
            }

            val classScheme = makeClassScheme(it)
            generateClass(classScheme)
        }
    }

    private fun makeClassScheme(annotatedClass: KSClassDeclaration): ClassScheme {
        val variables = annotatedClass.getDeclaredProperties().map { prop ->
            Variable(
                typeName = prop.makeTypeName(),
                name = prop.simpleName.asString(),
                flowableAnnotation = getPropertyFlowableAnnotation(prop)
            )
        }.toList()

        return ClassScheme(
            qualifiedName = annotatedClass.qualifiedName!!.asString(),
            variables = variables
        )
    }

    private fun generateClass(classScheme: ClassScheme) {
        val fileSpec = sourceBuilder.buildByClassScheme(classScheme)

        val codeWriter = codeGenerator.createNewFile(
            Dependencies.ALL_FILES,
            fileSpec.packageName,
            fileSpec.name,
        ).writer()

        fileSpec.writeTo(codeWriter)

        codeWriter.flush()
        codeWriter.close()
    }

    private fun getPropertyFlowableAnnotation(
        property: KSPropertyDeclaration
    ): Annotation? {
        for (annotationClass in FLOWABLE_ANNOTATIONS) {
            val annotation = property.getAnnotationsByType(annotationClass).firstOrNull()

            if (annotation != null)
                return annotation
        }

        return null
    }

    private fun KSPropertyDeclaration.makeTypeName() = type.resolveType()

    private fun KSTypeReference.resolveType(): TypeName {
        val resolvedType = resolve()

        return if (resolvedType.arguments.isEmpty())
            resolvedType.resolveSimpleType()
        else
            resolvedType.resolveParameterizedType()
    }


    private fun KSType.resolveSimpleType() = TypeName(
        packageName = declaration.packageName.asString(),
        className = declaration.simpleName.asString()
    )

    private fun KSType.resolveParameterizedType() = ParameterizedTypeName(
        packageName = declaration.packageName.asString(),
        className = declaration.simpleName.asString(),
        componentTypeNames = arguments.map {
            it.type?.resolveType() ?: throw error("Unexpected null type")
        }
    )
}

@KspExperimental
class FlowableProcessorProvider : SymbolProcessorProvider {

    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        init(environment)
        return FlowableProcessor(
            environment.codeGenerator
        )
    }

    private fun init(environment: SymbolProcessorEnvironment) {
        GlobalLogger.init(environment.logger)
    }
}
