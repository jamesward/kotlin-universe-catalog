package com.jamesward.kotlinuniversecatalog

import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings
import java.lang.IllegalStateException
import java.util.Properties

@Suppress("UnstableApiUsage")
class GradlePlugin : Plugin<Settings> {
    override fun apply(settings: Settings) {
        settings.gradle.settingsEvaluated {
            settings.dependencyResolutionManagement {
                if (repositories.isEmpty()) {
                    repositories.mavenCentral()
                    repositories.google()
                }

                val props = Properties()
                GradlePlugin::class.java.getResourceAsStream("/META-INF/kotlin-universe-catalog.properties")?.use {
                    props.load(it)
                }

                val pluginVersion = props.getProperty("version")

                if (pluginVersion.isNullOrEmpty()) {
                    throw IllegalStateException("Could not find version of the plugin")
                }
                else {
                    versionCatalogs {
                        create("universe") {
                            from("com.jamesward.kotlin-universe-catalog:stables:$pluginVersion")
                        }

                        create("universeunstable") {
                            from("com.jamesward.kotlin-universe-catalog:unstables:$pluginVersion")
                        }
                    }
                }
            }
        }
    }
}
