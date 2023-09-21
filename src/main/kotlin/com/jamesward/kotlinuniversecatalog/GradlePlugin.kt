package com.jamesward.kotlinuniversecatalog

import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings

@Suppress("UnstableApiUsage")
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
                        plugin("kotlin-jvm", "org.jetbrains.kotlin.jvm").version("1.9.10")
                        plugin("kotlin-android", "org.jetbrains.kotlin.android").version("1.9.10")
                        plugin("kotlin-multiplatform", "org.jetbrains.kotlin.multiplatform").version("1.9.10")
                        plugin("kotlin-plugin-serialization", "org.jetbrains.kotlin.plugin.serialization").version("1.9.0")

                        plugin("android-application", "com.android.application").version("8.1.0")
                        plugin("android-library", "com.android.library").version("8.1.0")

                        plugin("ksp", "com.google.devtools.ksp").version("1.9.10-1.0.13")

                        plugin("skie", "co.touchlab.skie").version("0.4.19")

                        plugin("jib", "com.google.cloud.tools.jib").version("3.4.0")

                        plugin("gradle-plugin-publish", "com.gradle.plugin-publish").version("1.2.1")
                        plugin("qoomon-git-versioning", "me.qoomon.git-versioning").version("6.4.2")
                        plugin("gradle-nexus-publish-plugin", "io.github.gradle-nexus.publish-plugin").version("1.1.0")

                        version("androidx-compose-compiler", "1.5.3")

                        library("kotlinx-serialization-json", "org.jetbrains.kotlinx", "kotlinx-serialization-json").version("1.6.0")
                        library("kotlinx-coroutines-core", "org.jetbrains.kotlinx", "kotlinx-coroutines-core").version("1.7.3")
                        library("kotlin-test", "org.jetbrains.kotlin", "kotlin-test").version("1.9.10")

                        library("jib-native-image-extension", "com.google.cloud.tools", "jib-native-image-extension-gradle").version("0.1.0")

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

                        library("ktor-server-core", "io.ktor", "ktor-server-core").version("2.3.4")
                        library("ktor-server-cio", "io.ktor", "ktor-server-cio").version("2.3.4")
                        library("ktor-server-content-negotiation", "io.ktor", "ktor-server-content-negotiation").version("2.3.4")
                        library("ktor-serialization-kotlinx-json", "io.ktor", "ktor-serialization-kotlinx-json").version("2.3.4")

                        library("arrow-kt-suspendapp", "io.arrow-kt", "suspendapp").version("0.4.0")
                        library("arrow-kt-suspendapp-ktor", "io.arrow-kt", "suspendapp-ktor").version("0.4.0")


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
