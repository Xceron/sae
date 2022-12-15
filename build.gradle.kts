import com.google.protobuf.gradle.id
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.0-Beta"
    application
    id("com.google.protobuf") version ("0.9.1")
}

val protobufVersion by extra("3.21.11")
val protobufPluginVersion by extra("0.8.14")
val grpcVersion by extra("1.51.1")
val grpcKotlinVersion by extra("1.3.0")

group = "de.florianbrand"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("io.github.evanrupert:excelkt:1.0.2")
    implementation("io.grpc:grpc-protobuf:$grpcVersion")
    implementation("io.grpc:grpc-stub:$grpcVersion")
    implementation("com.google.protobuf:protobuf-kotlin:$protobufVersion")
    implementation("com.google.protobuf:protobuf-java-util:$protobufVersion")
    implementation("io.grpc:grpc-kotlin-stub:$grpcKotlinVersion")
    implementation("io.grpc:grpc-core:$grpcVersion")
    implementation("io.grpc:grpc-netty-shaded:$grpcVersion")
}


tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.register<JavaExec>("runServer") {
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("de.florianbrand.NumberSquareServerKt")
}

tasks.register<JavaExec>("runClient") {
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("de.florianbrand.NumberSquareClientKt")
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:$protobufVersion"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:$grpcVersion"
        }
        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:$grpcKotlinVersion:jdk8@jar"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                id("grpc")
                id("grpckt")
            }
            it.builtins {
                id("kotlin")
            }
        }
    }
}