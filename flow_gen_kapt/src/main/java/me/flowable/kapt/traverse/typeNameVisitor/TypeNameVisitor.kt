package me.flowable.kapt.traverse.typeNameVisitor

import me.flowable.kapt.traverse.typeName.TypeName
import me.flowable.kapt.traverse.typeName.ParameterizedTypeName

interface TypeNameVisitor<T> {
    fun acceptTypeName(typeName: TypeName): T

    fun acceptParameterizedTypeName(typeName: ParameterizedTypeName): T
}
