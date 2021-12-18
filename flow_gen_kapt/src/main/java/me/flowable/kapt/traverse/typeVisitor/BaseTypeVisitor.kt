package me.flowable.kapt.traverse.typeVisitor

import me.flowable.kapt.logger.GlobalLogger
import me.flowable.kapt.traverse.typeName.TypeName
import javax.lang.model.type.*

abstract class BaseTypeVisitor : TypeVisitor<TypeName, Unit> {

    override fun visit(type: TypeMirror?, u: Unit?): TypeName {
        throw unsupportedTypeError(type)
    }

    override fun visitNull(type: NullType?, u: Unit?): TypeName {
        throw unsupportedTypeError(type)
    }

    override fun visitError(type: ErrorType?, u: Unit?): TypeName {
        throw unsupportedTypeError(type)
    }

    override fun visitNoType(type: NoType?, u: Unit?): TypeName {
        throw unsupportedTypeError(type)
    }

    override fun visitUnknown(type: TypeMirror?, u: Unit?): TypeName {
        throw unsupportedTypeError(type)
    }

    override fun visitTypeVariable(type: TypeVariable?, u: Unit?): TypeName {
        throw unsupportedTypeError(type)
    }

    override fun visitWildcard(type: WildcardType?, u: Unit?): TypeName {
        throw unsupportedTypeError(type)
    }

    override fun visitUnion(type: UnionType?, u: Unit?): TypeName {
        throw unsupportedTypeError(type)
    }

    override fun visitExecutable(type: ExecutableType?, u: Unit?): TypeName {
        throw unsupportedTypeError(type)
    }

    override fun visitIntersection(type: IntersectionType?, p1: Unit?): TypeName {
        throw unsupportedTypeError(type)
    }

    protected fun unsupportedTypeError(type: TypeMirror?): Nothing {
        val errorText = "$type is not supported"

        GlobalLogger.logError(errorText)

        throw error(errorText)
    }
}

fun TypeMirror.accept(visitor: BaseTypeVisitor): TypeName = accept(visitor, Unit)
