plugins {
    kotlin("jvm") version "2.0.21"
    application
}

group = "id.ac.praktikum.pbo"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

application {
    mainClass.set("MainKt")
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}

// Supaya keyboard bisa dibaca saat ./gradlew run
tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}