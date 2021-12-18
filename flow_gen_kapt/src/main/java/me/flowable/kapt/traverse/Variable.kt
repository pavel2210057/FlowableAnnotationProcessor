package me.flowable.kapt.traverse

import me.flowable.kapt.traverse.typeName.TypeName

data class Variable(
    val typeName: TypeName,
    val name: String,
    val flowableAnnotation: Annotation?
)
