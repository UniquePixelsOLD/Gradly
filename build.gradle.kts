import org.gradle.internal.impldep.org.apache.http.entity.ContentType.create

plugins {
    kotlin("jvm") version "1.9.22"
    `java-gradle-plugin`
}


val ktor_version: String by project

group = "net.uniquepixels"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

gradlePlugin {
    plugins {
        create("Gradly") {
            id = "net.uniquepixels.gradly"
            implementationClass = "net.uniquepixels.gradly.GradlyKt"
        }
    }
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    implementation(gradleApi())

    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}