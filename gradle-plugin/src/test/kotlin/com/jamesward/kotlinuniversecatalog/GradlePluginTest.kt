package com.jamesward.kotlinuniversecatalog

import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.nio.file.Files
import kotlin.io.path.div
import kotlin.io.path.writeText


class GradlePluginTest {

    private val testProjectDir = Files.createTempDirectory("gradle-plugin-test")
    private val buildFile = testProjectDir / "build.gradle.kts"
    private val settingsFile = testProjectDir / "settings.gradle.kts"

    //@Test
    fun works() {
        buildFile.writeText("""
            plugins {
                alias(universe.plugins.kotlin.jvm)
            }

            dependencies {
                implementation(universe.kotlinx.coroutines.core)
            }
        """.trimIndent())

        // todo: removing the version won't work because it is used for the dependencies on the version catalog artifacts. but we need to find a better way
        settingsFile.writeText("""
            pluginManagement {
                repositories {
                    mavenLocal()
                    mavenCentral()
                    gradlePluginPortal()
                }
            }
            
            @Suppress("UnstableApiUsage")
            dependencyResolutionManagement {
                repositories {
                    mavenLocal()
                    mavenCentral()
                }
            }
            
            plugins {
                id("com.jamesward.kotlin-universe-catalog") version "2023.09.26-4"
            }
        """.trimIndent())

        val result = GradleRunner.create()
            .withProjectDir(testProjectDir.toFile())
            .withPluginClasspath()
            .withArguments("dependencies")
            .build()

        assertTrue(result.output.contains("+--- org.jetbrains.kotlin:kotlin-stdlib-jdk8"))
        assertTrue(result.output.contains("\\--- org.jetbrains.kotlinx:kotlinx-coroutines-core"))
    }

}
