plugins {
    kotlin("jvm")
    `maven-publish`
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "me.flowable"
            artifactId = "ksp"
            version = "1.1.1"

            from(components["java"])
        }
    }
}

dependencies {

    implementation(project(":core"))
    implementation("com.google.devtools.ksp:symbol-processing-api:1.6.10-1.0.2")

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.0")
}
