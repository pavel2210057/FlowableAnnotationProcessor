package me.flowable.kapt.ext

fun String.uppercaseFirstChar() = this.replaceFirstChar { it.uppercaseChar() }
