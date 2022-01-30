package me.flowable.kapt.traverse.typeVisitor

import me.flowable.core.internal.traverse.typeName.TypeName
import me.flowable.core.internal.traverse.typeName.ParameterizedTypeName
import javax.lang.model.type.*

class JavaToKotlinTypeVisitor : BaseTypeVisitor() {

    private companion object {
        private const val KOTLIN_STD_PACKAGE = "kotlin"
        private const val KOTLIN_COLLECTIONS_PACKAGE = "kotlin.collections"

        private val JAVA_TO_KOTLIN_STD_CLASSES_MAP = mapOf(
            "java.lang.Boolean"     to "Boolean",
            "java.lang.Integer"     to "Int",
            "java.lang.Byte"        to "Byte",
            "java.lang.Character"   to "Char",
            "java.lang.Short"       to "Short",
            "java.lang.Long"        to "Long",
            "java.lang.Float"       to "Float",
            "java.lang.Double"      to "Double",
            "java.lang.String"      to "String"
        )

        private val JAVA_TO_KOTLIN_STD_COLLECTIONS_MAP = mapOf(
            "java.util.Collection" to "Collection",
            "java.util.List" to "List",
            "java.util.Map" to "Map",
            "java.util.Set" to "Set",
            "java.util.ArrayList" to "ArrayList",
            "java.util.LinkedHashMap" to "LinkedHashMap",
            "java.util.HashMap" to "HashMap",
            "java.util.LinkedHashSet" to "LinkedHashSet",
            "java.util.HashSet" to "HashSet",
            "java.lang.Iterable" to "Iterable",
            "java.util.Iterator" to "Iterator",
            "java.util.ListIterator" to "ListIterator"
        )
    }

    override fun visitPrimitive(type: PrimitiveType, u: Unit?): TypeName {
        return when (type.kind) {
            TypeKind.BOOLEAN -> TypeName(KOTLIN_STD_PACKAGE, "Boolean")
            TypeKind.INT -> TypeName(KOTLIN_STD_PACKAGE, "Int")
            TypeKind.BYTE -> TypeName(KOTLIN_STD_PACKAGE, "Byte")
            TypeKind.CHAR -> TypeName(KOTLIN_STD_PACKAGE, "Char")
            TypeKind.SHORT -> TypeName(KOTLIN_STD_PACKAGE, "Short")
            TypeKind.LONG -> TypeName(KOTLIN_STD_PACKAGE, "Long")
            TypeKind.FLOAT -> TypeName(KOTLIN_STD_PACKAGE, "Float")
            TypeKind.DOUBLE -> TypeName(KOTLIN_STD_PACKAGE, "Double")
            else -> throw unsupportedTypeError(type)
        }
    }

    override fun visitArray(type: ArrayType, u: Unit?): TypeName {
        val componentTypeName = type.componentType.accept(this)
        return ParameterizedTypeName(KOTLIN_STD_PACKAGE, "Array", listOf(componentTypeName))
    }

    override fun visitDeclared(type: DeclaredType, u: Unit?): TypeName {
        val qualifiedTypeName = type.toString()

        makeExtraStdClassTypeNameIfNeeded(qualifiedTypeName)?.let { return it }

        val typeArgs = type.typeArguments.map { it.accept(this) }
        if (typeArgs.isNotEmpty()) {
            makeStdCollectionTypeNameIfNeeded(qualifiedTypeName, typeArgs)?.let { return it }

            return ParameterizedTypeName(
                qualifiedTypeName.substringBeforeLast('.'),
                qualifiedTypeName.substringAfterLast('.'),
                typeArgs
            )
        }
        else {
            return TypeName(
                qualifiedTypeName.substringBeforeLast('.'),
                qualifiedTypeName.substringAfterLast('.')
            )
        }
    }

    private fun makeExtraStdClassTypeNameIfNeeded(qualifiedTypeName: String): TypeName? {
        val kotlinClassName = JAVA_TO_KOTLIN_STD_CLASSES_MAP[qualifiedTypeName] ?: return null
        return TypeName(KOTLIN_STD_PACKAGE, kotlinClassName)
    }

    private fun makeStdCollectionTypeNameIfNeeded(
        qualifiedTypeName: String,
        typeArgs: List<TypeName>
    ): TypeName? {
        val kotlinClassName =
            JAVA_TO_KOTLIN_STD_COLLECTIONS_MAP[qualifiedTypeName.substringBefore('<')]
                ?: return null
        return ParameterizedTypeName(
            KOTLIN_COLLECTIONS_PACKAGE,
            kotlinClassName,
            typeArgs
        )
    }
}
