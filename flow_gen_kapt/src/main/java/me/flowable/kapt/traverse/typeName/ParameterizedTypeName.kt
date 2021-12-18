package me.flowable.kapt.traverse.typeName

import me.flowable.kapt.traverse.typeNameVisitor.TypeNameVisitor

class ParameterizedTypeName(
    packageName: String,
    className: String,
    val componentTypeNames: List<TypeName>
) : TypeName(packageName, className) {

    override fun <T> accept(visitor: TypeNameVisitor<T>) = visitor.acceptParameterizedTypeName(this)
}
