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

    private val testRepo = System.getProperty("test-repo")
    private val pluginVersion = System.getProperty("plugin-version")

    @Test
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
                    maven(uri("$testRepo"))
                    mavenCentral()
                }
            }
            
            @Suppress("UnstableApiUsage")
            dependencyResolutionManagement {
                repositories {
                    maven(uri("$testRepo"))
                    mavenCentral()
                }
            }
            
            plugins {
                id("com.jamesward.kotlin-universe-catalog") version "$pluginVersion"
            }
        """.trimIndent())

        val result = GradleRunner.create()
            .withProjectDir(testProjectDir.toFile())
            .withArguments("dependencies")
            .build()

        assertTrue(result.output.contains("+--- org.jetbrains.kotlin:kotlin-stdlib-jdk8"))
        assertTrue(result.output.contains("\\--- org.jetbrains.kotlinx:kotlinx-coroutines-core"))
    }

}
