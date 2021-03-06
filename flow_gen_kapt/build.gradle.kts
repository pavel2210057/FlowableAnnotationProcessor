plugins {
    kotlin("jvm")
    kotlin("kapt")
}

dependencies {

    implementation(project(":core"))
    implementation("com.google.auto.service:auto-service:1.0-rc5")
    kapt("com.google.auto.service:auto-service:1.0-rc5")

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.0")
}
