package com.jamesward.kotlinuniversecatalog

import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings

class GradlePlugin : Plugin<Settings> {
    override fun apply(settings: Settings) {
        settings.gradle.settingsEvaluated {
            settings.dependencyResolutionManagement {
                if (repositories.isEmpty()) {
                    repositories.mavenCentral()
                    repositories.google()
                }

                versionCatalogs {
                    create("universe") {
                        // todo: read from this project's catalog / build so that we can get dependencybot updates
                        plugin("kotlin-jvm", "org.jetbrains.kotlin.jvm").version("1.9.10")
                        plugin("kotlin-android", "org.jetbrains.kotlin.android").version("1.9.10")
                        plugin("kotlin-multiplatform", "org.jetbrains.kotlin.multiplatform").version("1.9.10")

                        plugin("android-application", "com.android.application").version("8.1.0")
                        plugin("android-library", "com.android.library").version("8.1.0")

                        plugin("ksp", "com.google.devtools.ksp").version("1.9.10-1.0.13")

                        plugin("skie", "co.touchlab.skie").version("0.4.19")

                        version("androidx-compose-compiler", "1.5.3")

                        library("kotlinx-coroutines-core", "org.jetbrains.kotlinx", "kotlinx-coroutines-core").version("1.7.3")
                        library("kotlin-test", "org.jetbrains.kotlin", "kotlin-test").version("1.9.10")

                        library("androidx-compose-bom", "androidx.compose", "compose-bom").version("2023.06.00")
                        library("androidx-compose-material", "androidx.compose.material", "material").withoutVersion()
                        library("androidx-compose-ui", "androidx.compose.ui", "ui").withoutVersion()
                        library("androidx-compose-ui-tooling", "androidx.compose.ui", "ui-tooling").withoutVersion()
                        library("androidx-compose-ui-tooling-preview", "androidx.compose.ui", "ui-tooling-preview").withoutVersion()

                        library("androidx-lifecycle-runtime-compose", "androidx.lifecycle", "lifecycle-runtime-compose").version("2.6.1")
                        library("androidx-lifecycle-viewmodel-compose", "androidx.lifecycle", "lifecycle-viewmodel-compose").version("2.6.1")
                        library("androidx-lifecycle-runtime-kts", "androidx.lifecycle", "lifecycle-runtime-ktx").version("2.6.1")

                        library("androidx-activity-compose", "androidx.activity", "activity-compose").version("1.7.2")
                        library("androidx-core-ktx", "androidx.core", "core-kts").version("1.10.1")

                        // beta

                        plugin("beta-kotlin-jvm", "org.jetbrains.kotlin.jvm").version("1.9.10-Beta")
                        plugin("beta-kotlin-android", "org.jetbrains.kotlin.android").version("1.9.10-Beta")
                        plugin("beta-kotlin-multiplatform", "org.jetbrains.kotlin.multiplatform").version("1.9.10-Beta")

                        plugin("beta-android-application", "com.android.application").version("8.2.0-beta04")
                        plugin("beta-android-library", "com.android.library").version("8.2.0-beta04")

                        // alpha

                        plugin("alpha-android-application", "com.android.application").version("8.3.0-alpha04")
                        plugin("alpha-android-library", "com.android.library").version("8.3.0-alpha04")

                        library("alpha-androidx-datastore-core-okio", "androidx.datastore", "datastore-core-okio").version("1.1.0-alpha04")
                        library("alpha-androidx-datastore-preferences-core", "androidx.datastore", "datastore-preferences-core").version("1.1.0-alpha04")
                    }

                }
            }
        }
    }
}
