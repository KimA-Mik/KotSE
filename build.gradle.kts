plugins {
    kotlin("jvm") version "1.8.0"
    kotlin("plugin.serialization") version "1.8.10"
    application
}

group = "my.code"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = uri("https://repo.e-iceblue.cn/repository/maven-public/") }
}

dependencies {
    testImplementation(kotlin("test"))
    // https://mvnrepository.com/artifact/e-iceblue/spire.pdf
    implementation("e-iceblue:spire.pdf:9.1.4")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("MainKt")
}