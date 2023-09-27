package com.jamesward.kotlinuniversecatalog

import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings
import org.gradle.api.internal.artifacts.dsl.dependencies.DefaultDependencyHandler
import org.gradle.api.internal.plugins.DefaultPluginManager
import org.gradle.kotlin.dsl.findPlugin
import org.gradle.plugin.management.internal.DefaultPluginManagementSpec
import org.gradle.plugin.management.internal.DefaultPluginResolutionStrategy
import java.lang.IllegalStateException

@Suppress("UnstableApiUsage")
class GradlePlugin : Plugin<Settings> {
    override fun apply(settings: Settings) {
        settings.gradle.settingsEvaluated {
            settings.dependencyResolutionManagement {
                if (repositories.isEmpty()) {
                    repositories.mavenCentral()
                    repositories.google()
                }

                // so bad. how else to get the plugin version?
                val maybePluginLine = settings.buildscript.sourceFile?.readLines()?.find {
                    it.contains("id(\"com.jamesward.kotlin-universe-catalog\")")
                }

                val maybeVersion = maybePluginLine?.substringAfter(" version \"")?.takeWhile { it != '"' }
                maybeVersion?.let { version ->
                    versionCatalogs {
                        create("universe") {
                            from("com.jamesward.kotlin-universe-catalog:stables:$version")
                        }

                        create("universerc") {
                            from("com.jamesward.kotlin-universe-catalog:rcs:$version")
                        }

                        create("universebeta") {
                            from("com.jamesward.kotlin-universe-catalog:betas:$version")
                        }

                        create("universealpha") {
                            from("com.jamesward.kotlin-universe-catalog:alphas:$version")
                        }
                    }
                } ?: throw IllegalStateException("Could not find version of the plugin")
            }
        }
    }
}
