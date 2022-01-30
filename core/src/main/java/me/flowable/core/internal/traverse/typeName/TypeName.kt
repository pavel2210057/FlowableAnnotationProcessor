package me.flowable.core.internal.traverse.typeName

import me.flowable.core.internal.traverse.typeNameVisitor.TypeNameVisitor

open class TypeName(
    val packageName: String,
    val className: String
) {

    open fun<T> accept(visitor: TypeNameVisitor<T>) = visitor.acceptTypeName(this)

    override fun toString(): String = "TypeName(packageName=$packageName, className=$className)"
}
