package me.flowable.core.internal.traverse.typeNameVisitor

import me.flowable.core.internal.traverse.typeName.TypeName
import me.flowable.core.internal.traverse.typeName.ParameterizedTypeName

interface TypeNameVisitor<T> {
    fun acceptTypeName(typeName: TypeName): T

    fun acceptParameterizedTypeName(typeName: ParameterizedTypeName): T
}
