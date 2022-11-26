import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("maven-publish")
    id("signing")
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.4.1")
}

kotlin {
    explicitApi()
    jvmToolchain(11)
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs = freeCompilerArgs +
                "-opt-in=kotlinx.serialization.ExperimentalSerializationApi"
    }
}

signing {
    if (findProperty("signing.secretKeyRingFile") != null) {
        sign(publishing.publications)
    }
}

publishing {
    publications {
        register(project.name, MavenPublication::class) {
            artifact(tasks["kotlinSourcesJar"])
            artifact(tasks["jar"]) {
                classifier = null
            }

            pom {
                name.set("partial-kt")
                description.set("A Kotlin KSP plugin for generating partial variants of classes.")
                url.set("https://github.com/MateriApps/partial-kt")
                licenses {
                    name.set("The Apache License, Version 2.0")
                    url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                }
                developers {
                    developer {
                        id.set("rushii")
                        name.set("rushii")
                        url.set("https://github.com/DiamondMiner88/")
                        email.set("vdiamond_@outlook.com")
                    }
                    developer {
                        id.set("xinto")
                        name.set("Xinto")
                        url.set("https://github.com/X1nto/")
                    }
                }
                scm {
                    url.set("https://github.com/MateriApps/partial-kt")
                    connection.set("scm:git:github.com/MateriApps/partial-kt.git")
                    developerConnection.set("scm:git:ssh://github.com/MateriApps/partial-kt.git")
                }
            }
        }
    }
    repositories {
        val sonatypeUsername = System.getenv("SONATYPE_USERNAME")
        val sonatypePassword = System.getenv("SONATYPE_PASSWORD")

        if (sonatypeUsername == null || sonatypePassword == null)
            mavenLocal()
        else {
            maven {
                credentials {
                    username = sonatypeUsername
                    password = sonatypePassword
                }
                setUrl("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            }
        }
    }
}
