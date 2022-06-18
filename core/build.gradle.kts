plugins {
    kotlin("jvm")
    `maven-publish`
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "me.flowable"
            artifactId = "core"
            version = "1.1.1"

            from(components["java"])
        }
    }
}

dependencies {

    api("com.squareup:kotlinpoet:+")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0-RC")
}
