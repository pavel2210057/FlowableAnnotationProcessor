package me.flowable.kapt.traverse.typeName

import me.flowable.kapt.traverse.typeNameVisitor.TypeNameVisitor

open class TypeName(
    val packageName: String,
    val className: String
) {

    open fun<T> accept(visitor: TypeNameVisitor<T>) = visitor.acceptTypeName(this)
}
