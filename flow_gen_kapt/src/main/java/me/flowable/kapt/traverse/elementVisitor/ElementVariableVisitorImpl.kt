package me.flowable.kapt.traverse.elementVisitor

import me.flowable.core.Shared
import me.flowable.core.State
import me.flowable.core.Skip
import me.flowable.core.internal.traverse.Variable
import me.flowable.core.internal.traverse.typeName.TypeName
import me.flowable.kapt.traverse.typeVisitor.JavaToKotlinTypeVisitor
import me.flowable.kapt.traverse.typeVisitor.accept
import javax.lang.model.element.*
import javax.lang.model.type.TypeMirror

class ElementVariableVisitorImpl : BaseElementVariableVisitor() {

    private companion object {
        val FLOWABLE_ANNOTATIONS = arrayOf(
            State::class.java,
            Shared::class.java,
            Skip::class.java
        )
    }

    private val typeVisitor = JavaToKotlinTypeVisitor()

    override fun visitVariable(element: VariableElement, u: Unit?) =
        Variable(
            typeName = getTypeName(element.asType()),
            name = element.simpleName.toString(),
            flowableAnnotation = getFlowableAnnotation(element)
        )

    private fun getTypeName(typeMirror: TypeMirror): TypeName = typeMirror.accept(typeVisitor)

    private fun getFlowableAnnotation(element: VariableElement): Annotation? {
        for (annotationClass in FLOWABLE_ANNOTATIONS) {
            val annotation = element.getAnnotation(annotationClass)

            if (annotation != null)
                return annotation
        }

        return null
    }
}
