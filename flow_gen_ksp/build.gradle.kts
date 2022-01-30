plugins {
    kotlin("jvm")
}

dependencies {

    implementation(project(":core"))
    implementation("com.google.devtools.ksp:symbol-processing-api:1.5.31-1.0.0")

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.0")
}
