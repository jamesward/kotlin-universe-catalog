// needed to handle bump then build
pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        google()
        gradlePluginPortal()
        maven("https://oss.sonatype.org/content/groups/staging")  // since mavenCentral takes a little while
    }
}

// needed to handle bump then build
@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        google()
        // for resolving plugins as libraries
        gradlePluginPortal()
        maven("https://repo.gradle.org/gradle/libs-releases")
        maven("https://oss.sonatype.org/content/groups/staging")  // since mavenCentral takes a little while
    }
}

plugins {
    id("com.jamesward.kotlin-universe-catalog") version "2023.12.24-1"
}

include("stables", "unstables", "gradle-plugin")