package com.jamesward.kotlinuniversecatalog

import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings
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

                        create("universeunstable") {
                            from("com.jamesward.kotlin-universe-catalog:unstables:$version")
                        }
                    }
                } ?: throw IllegalStateException("Could not find version of the plugin")
            }
        }
    }
}
