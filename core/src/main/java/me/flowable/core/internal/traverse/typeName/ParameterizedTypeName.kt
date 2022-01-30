package me.flowable.core.internal.traverse.typeName

import me.flowable.core.internal.traverse.typeNameVisitor.TypeNameVisitor

class ParameterizedTypeName(
    packageName: String,
    className: String,
    val componentTypeNames: List<TypeName>
) : TypeName(packageName, className) {

    override fun <T> accept(visitor: TypeNameVisitor<T>) = visitor.acceptParameterizedTypeName(this)

    override fun toString() = "ParameterizedTypeName(packageName=$packageName, className=$className, componentTypeNames=$componentTypeNames)"
}
