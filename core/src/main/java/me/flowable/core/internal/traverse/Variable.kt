package me.flowable.core.internal.traverse

import me.flowable.core.internal.traverse.typeName.TypeName

data class Variable(
    val typeName: TypeName,
    val name: String,
    val flowableAnnotation: Annotation?
)
