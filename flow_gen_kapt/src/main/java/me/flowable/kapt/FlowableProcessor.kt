package me.flowable.kapt

import com.google.auto.service.AutoService
import me.flowable.core.Flowable
import me.flowable.core.internal.builder.FlowableSourceBuilder
import me.flowable.kapt.logger.GlobalLogger
import me.flowable.core.internal.traverse.ClassScheme
import me.flowable.kapt.traverse.elementVisitor.ElementVariableVisitorImpl
import me.flowable.kapt.traverse.elementVisitor.accept
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.*

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions("kapt.kotlin.generated")
@SupportedAnnotationTypes("me.flowable.core.Flowable")
class FlowableProcessor : AbstractProcessor() {

    private val elementVariableVisitor = ElementVariableVisitorImpl()
    private val sourceBuilder = FlowableSourceBuilder()

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)

        GlobalLogger.initLogger(processingEnv.messager)
    }

    override fun process(
        annotations: MutableSet<out TypeElement>,
        roundEnv: RoundEnvironment
    ): Boolean {
        if (roundEnv.processingOver())
            return false

        val flowableClasses = roundEnv.getElementsAnnotatedWith(Flowable::class.java)
        processFlowable(flowableClasses)

        return false
    }

    private fun processFlowable(flowableClasses: Set<Element>) {
        flowableClasses.forEach {
            if (!it.kind.isClass) {
                GlobalLogger.logError(
                    "Flowable annotation is not applicable to non-class entities"
                )
                return@forEach
            }

            val classScheme = makeClassScheme(it as TypeElement)
            generateClass(classScheme)
        }
    }

    private fun makeClassScheme(typeElement: TypeElement): ClassScheme {
        val variables = typeElement.enclosedElements.mapNotNull { element ->
            element.accept(elementVariableVisitor)
        }

        return ClassScheme(
            typeElement.qualifiedName.toString(),
            variables
        )
    }

    private fun generateClass(classScheme: ClassScheme) {
        val fileSpec = sourceBuilder.buildByClassScheme(classScheme)
        fileSpec.writeTo(processingEnv.filer)
    }
}
