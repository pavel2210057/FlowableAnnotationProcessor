package me.flowable.kapt.traverse.typeNameVisitor

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import me.flowable.kapt.traverse.typeName.TypeName
import com.squareup.kotlinpoet.TypeName as PoetTypeName
import me.flowable.kapt.traverse.typeName.ParameterizedTypeName

class TypeNameToKotlinPoetTypeNameVisitor : TypeNameVisitor<PoetTypeName> {

    override fun acceptTypeName(typeName: TypeName): PoetTypeName =
        ClassName(typeName.packageName, typeName.className)

    override fun acceptParameterizedTypeName(typeName: ParameterizedTypeName): PoetTypeName =
        ClassName(typeName.packageName, typeName.className)
            .parameterizedBy(
                typeName.componentTypeNames.map {
                    it.accept(this)
                }
            )
}
