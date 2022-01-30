package me.flowable.core.internal.ext

fun String.uppercaseFirstChar() = this.replaceFirstChar { it.uppercaseChar() }
