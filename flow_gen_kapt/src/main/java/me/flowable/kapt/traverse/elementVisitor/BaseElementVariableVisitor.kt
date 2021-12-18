package me.flowable.kapt.traverse.elementVisitor

import me.flowable.kapt.traverse.Variable
import javax.lang.model.element.*

abstract class BaseElementVariableVisitor : ElementVisitor<Variable?, Unit> {

    override fun visit(element: Element?, u: Unit?): Variable? = null
    override fun visitPackage(element: PackageElement?, u: Unit?): Variable? = null
    override fun visitType(element: TypeElement?, u: Unit?): Variable? = null
    override fun visitExecutable(element: ExecutableElement?, u: Unit?): Variable? = null
    override fun visitTypeParameter(element: TypeParameterElement?, u: Unit?): Variable? = null
    override fun visitUnknown(element: Element?, u: Unit?): Variable? = null
}

fun Element.accept(visitor: BaseElementVariableVisitor): Variable? = accept(visitor, Unit)
